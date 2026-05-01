import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { Recipe } from "../types/recipe";
import useFetch from "../Hooks/useFetch";
import Header from "../Components/Header";
import NavButton from "../Components/NavButton";
import { ArrowLeft, ChefHat, Clock, Flame, Plus, X, Users } from "lucide-react";
import type { Ingredient } from "./SavedRecipe";
import InfoCard from "../Components/InfoCard";
import TagComponent from "../Components/TagComponent";
import { config } from "../config";

function RecipeForm({ mode }: { mode: "create" | "edit"}) {
    const { id } = useParams();
    const navigate = useNavigate();
    const [recipe, setRecipe] = useState<Recipe>({
        title: "",
        description: "",
        ingredients: [],
        instructions: "",
        tags: [],
        prepTime: 0,
        cookTime: 0,
        servings: 0,
        calories: 0,
        image: "",
        visibility: "private",
        createdAt: null,
        updatedAt: null
    });
    const [imageFile, setImageFile] = useState<File | null>(null);
    const [imagePreview, setImagePreview] = useState<string | null>(null);

    const token = localStorage.getItem("token");
    const isEdit = mode === "edit" && id;
    
    const { data, loading, error } = useFetch<Recipe>(
        isEdit ? `${config.apiUrl}/api/recipes/me/${id}` : null,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    );
    
    useEffect(() => {
        if (isEdit && data) {
            setRecipe(data);
            setImageFile(null);
            setImagePreview(null);
        }
    }, [isEdit, data]);

    const getImageSrc = () => {
        if (imagePreview) return imagePreview;

        if (!recipe.image) return "default.png";

        if (recipe.image.startsWith("blob:")) {
            return recipe.image;
        }

        return `https://biteful-images.s3.eu-central-1.amazonaws.com/${recipe.image}`;
    };

    const [tagInput, setTagInput] = useState("");

    const addTag = () => {
        if (!tagInput.trim()) return;

        if (recipe.tags?.includes(tagInput.trim())) return;

        setRecipe({
            ...recipe,
            tags: [...(recipe.tags ?? []), tagInput.trim()],
        });

        setTagInput("");
    };

    const removeTag = (index: number) => {
        setRecipe({
            ...recipe,
            tags: recipe.tags?.filter((_, i) => i !== index) ?? [],
        });
    };

    const updateIngredient = (index: number, field: keyof Ingredient, value: any) => {
        const updated = [...recipe.ingredients];
        updated[index] = { ...updated[index], [field]: value };
        setRecipe({ ...recipe, ingredients: updated });
    };

    const addIngredient = () => {
        setRecipe({
        ...recipe,
        ingredients: [...recipe.ingredients, { name: "", quantity: 0, unit: "" }],
        });
    };

    const removeIngredient = (index: number) => {
        const updated = recipe.ingredients.filter((_, i) => i !== index);
        setRecipe({ ...recipe, ingredients: updated });
    };

    const handleSubmit = async () => {
        const token = localStorage.getItem("token");

        const method = isEdit ? "PUT" : "POST";

        const formData = new FormData();

        formData.append(
            "recipe",
            new Blob([JSON.stringify(recipe)], { type: "application/json" })
        );

        if (imageFile) {
            formData.append("image", imageFile);
        }

        const url = isEdit
            ? `${config.apiUrl}/api/recipes/me/${id}`
            : `${config.apiUrl}/api/recipes`;

        await fetch(url, {
            method,
            headers: {
                Authorization: `Bearer ${token}`,
            },
            body: formData,
        });

        navigate(isEdit ? location.pathname.replace(/\/edit$/, "") : "/saved")
    };

    if (loading) return <p>Loading...</p>;

    return (
        <>
            <div>{error}</div>
            <Header />
            <div className="min-h-screen bg-[#ffffff] pb-8 lg:px-24">

                {/* Header */}
                <div className="sticky top-0 z-10 bg-[#ffffff] border-b">
                    <div className="flex items-center justify-between px-4 py-2">
                        <NavButton to="/saved">
                            <ArrowLeft className="w-4 h-4" />
                        </NavButton>

                        <div className="flex gap-2">
                            <button
                                onClick={handleSubmit}
                                className="bg-green-500 text-white px-3 py-2 rounded-md"
                            >
                                {isEdit ? "Update" : "Create"}
                            </button>

                            <button
                                onClick={() =>
                                    navigate(isEdit ? location.pathname.replace(/\/edit$/, "") : "/saved")
                                }
                                className="px-3 py-2 rounded-md border"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>

                {/* Image */}
                <div
                    className="w-full h-64 md:h-96 bg-[#ececf0] -mt-4 lg:rounded-xl overflow-hidden cursor-pointer flex items-center justify-center"
                    onClick={() => document.getElementById("fileInput")?.click()}
                    >
                    {(recipe.image || imagePreview) ? (
                        <img
                        src={getImageSrc()}
                        className="w-full h-full object-cover"
                        />
                    ) : (
                        <span>Click to upload image</span>
                    )}

                    <input
                        id="fileInput"
                        type="file"
                        accept="image/*"
                        className="hidden"
                        onChange={(e) => {
                            const file = e.target.files?.[0];
                            if (!file) return;

                            setImageFile(file);

                            const preview = URL.createObjectURL(file);
                            setImagePreview(preview);
                        }}
                    />
                </div>

                <div className="mx-auto px-4 -mt-8 relative z-1">
                    <div className="bg-[#ffffff] flex flex-col gap-6 rounded-xl border p-6 md:p-8 shadow-lg">

                        {/* Title */}
                        <h2 className="text-3xl md:text-4xl mb-4">
                            <input
                                value={recipe.title}
                                onChange={(e) => setRecipe({ ...recipe, title: e.target.value })}
                                placeholder="Title"
                                className="text-3xl md:text-4xl border-b outline-none"
                            />
                        </h2>

                        {/* Description */}
                        <textarea
                            value={recipe.description ?? ""}
                            onChange={(e) => setRecipe({ ...recipe, description: e.target.value })}
                            placeholder="Description"
                            className="text-[#717182] text-lg border last:mb-0 p-3 rounded-md"
                        />

                        {/* Tags */}
                        <h3>Tags</h3>
                        <div className="flex flex-col gap-3">

                            {/* input + button */}
                            <div className="flex items-center gap-2">
                                <div>
                                    <input
                                        value={tagInput}
                                        onChange={(e) => setTagInput(e.target.value)}
                                        onKeyDown={(e) => {
                                            if (e.key === "Enter") {
                                                e.preventDefault();
                                                addTag();
                                            }
                                        }}
                                        placeholder="Add tag"
                                        className="border p-2 rounded flex-1"
                                    />
                                </div>

                                <button
                                    onClick={addTag}
                                    className="p-2 rounded-md border hover:bg-gray-100"
                                >
                                    <Plus className="w-4 h-4" />
                                </button>
                            </div>

                            {/* tags list */}
                            {recipe.tags && recipe.tags.length > 0 && (
                                <div className="flex flex-wrap gap-2">
                                    {recipe.tags.map((tag, index) => (
                                        <TagComponent
                                            key={index}
                                            label={tag}
                                            editable
                                            onRemove={() => removeTag(index)}
                                        />
                                    ))}
                                </div>
                            )}
                        </div>
                        

                        {/* Stats */}
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
                            <InfoCard
                                text="Prep"
                                val={`${recipe.prepTime ?? 0}`}
                                icon={Clock}
                                color="#030213"
                                editable
                                onChange={(value) =>
                                    setRecipe({ ...recipe, prepTime: value })
                                }
                            />
                            <InfoCard
                                text="Cook"
                                val={`${recipe.cookTime ?? 0}`}
                                icon={ChefHat}
                                color="#ffa600"
                                editable
                                onChange={(value) =>
                                    setRecipe({ ...recipe, cookTime: value })
                                }
                            />
                            <InfoCard
                                text="Servings"
                                val={`${recipe.servings ?? 0}`}
                                icon={Users}
                                color="#00ff2f"
                                editable
                                onChange={(value) =>
                                    setRecipe({ ...recipe, servings: value })
                                }
                            />
                            <InfoCard
                                text="Calories"
                                val={`${recipe.calories ?? 0}`}
                                icon={Flame}
                                color="#ff0d00"
                                editable
                                onChange={(value) =>
                                    setRecipe({ ...recipe, calories: value })
                                }
                            />
                        </div>

                        {/* Ingredients */}
                        <div className="mb-8">
                            <h3 className="mb-4 flex justify-between items-center">
                                <div>
                                    <span>Ingredients - </span>
                                    <span className="text-xl bg-[#f3f3f3] rounded-lg px-2">{recipe.ingredients.length}</span>
                                </div>
                                
                                <button onClick={addIngredient}>
                                    <Plus />
                                </button>
                            </h3>

                            <div className="bg-[#eeeeee] rounded-lg p-4 space-y-3">
                                {recipe.ingredients.map((ing, i) => (
                                <div key={i} className="flex gap-2 items-center">
                                    <input
                                        value={ing.name}
                                        onChange={(e) =>
                                            updateIngredient(i, "name", e.target.value)
                                        }
                                        placeholder="Name"
                                        className="flex-1 rounded-lg p-2"
                                    />

                                    <input
                                        type="number"
                                        value={ing.quantity ?? ""}
                                        onChange={(e) =>
                                            updateIngredient(i, "quantity", Number(e.target.value))
                                        }
                                        placeholder="quantity"
                                        className="w-40 rounded-lg p-2"
                                    />

                                    <input
                                        value={ing.unit ?? ""}
                                        onChange={(e) =>
                                            updateIngredient(i, "unit", e.target.value)
                                        }
                                        placeholder="unit"
                                        className="w-40 rounded-lg p-2"
                                    />

                                    <button onClick={() => removeIngredient(i)}>
                                        <X />
                                    </button>
                                </div>
                                ))}
                            </div>
                        </div>

                        {/* Instructions */}
                        <div className="mb-6">
                            <h3 className="mb-4">Instructions</h3>


                            <div className="bg-[#eeeeee] rounded-lg p-4 space-y-3">
                                <textarea
                                    value={recipe.instructions ?? ""}
                                    onChange={(e) =>
                                        setRecipe({ ...recipe, instructions: e.target.value })
                                    }
                                    placeholder="Instructions (one step per line)"
                                    className="w-full h-40 rounded-lg p-4 space-y-3"
                                />
                            </div>
                            
                        </div>

                        {/* Privacy */}
                        <div className="mb-6">
                            <h3 className="mb-4">Privacy settings</h3>

                            <div className="flex items-center gap-2">
                                <button
                                    onClick={() =>
                                        setRecipe({ ...recipe, visibility: "private" })
                                    }
                                    className={`px-3 py-2 rounded-md border transition
                                        ${recipe.visibility === "private"
                                            ? "bg-[#030213] text-white"
                                            : "hover:bg-gray-100"
                                        }`
                                    }
                                >
                                    Private
                                </button>

                                <button
                                    onClick={() =>
                                        setRecipe({ ...recipe, visibility: "public" })
                                    }
                                    className={`px-3 py-2 rounded-md border transition
                                        ${recipe.visibility === "public"
                                            ? "bg-[#030213] text-white"
                                            : "hover:bg-gray-100"
                                        }`
                                    }
                                >
                                    Public
                                </button>
                            </div>
                        </div>
                        

                    </div>
                </div>
            </div>
        </>
    );

}

export default RecipeForm;
import { useNavigate, useParams } from "react-router-dom";
import Header from "../Components/Header";
import NavButton from "../Components/NavButton";

import { ArrowLeft, Clock, ChefHat, Flame, Users } from "lucide-react";
import InfoCard from "../Components/InfoCard";
import useFetch from "../Hooks/useFetch";
import type { Recipe } from "../types/recipe";
import TagComponent from "../Components/TagComponent";
import { config } from "../config";
import { useEffect, useState } from "react";

export type Ingredient = {
    name: string;
    quantity: number;
    unit?: string;
}

function SavedRecipe({ mode = "saved" } : {mode?: "saved" | "public" | "home"}) {
    const { id } = useParams();
    const navigate = useNavigate();

    const api =
        mode === "saved"
            ? "/api/recipes/me/"
            : "/api/recipes/"

    const token = localStorage.getItem("token");
    const { data, loading, error } = useFetch<Recipe>(
        `${config.apiUrl}${api}${id}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    )

    const handleDelete = async () => {
        if (!id) return;
        if (!confirm("Are you sure you want to delete the recipe?")) return;

        await fetch(`${config.apiUrl}/api/recipes/me/${id}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        navigate("/saved");
    };

    const handleSave = async () => {
        if (!id) return;

        const response = await fetch(`${config.apiUrl}/api/recipes/save/${id}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const data = await response.json();

        navigate(`/saved/${data.id}`);
    }

    const baseServings = data?.servings && data.servings > 0 ? data.servings : 1;
    const [servings, setServings] = useState(data?.servings ?? 0);

    useEffect(() => {
        if (data?.servings != null) {
            setServings(data?.servings === 0 ? 1 : data?.servings);
        }
    }, [data]);

    const scale = baseServings > 0 ? servings / baseServings : 1;

    const scaledIngredients = data?.ingredients.map((ing) => ({
        ...ing,
        quantity: ing.quantity * scale,
    }));

    const formatQuantity = (qty: number) => {
        return Number.isInteger(qty)
            ? qty
            : Number(qty.toFixed(2));
    };

    if (loading) return <p>Loading</p>;
    if (error) return <p>{error}</p>
    if (!data) return <p>No data</p>;

    return(
        <>
            <Header />
            <div className="min-h-screen bg-[#ffffff] pb-8 lg:px-24">
                
                {/* Header with return button */}
                <div className="sticky top-0 z-10 bg-[#ffffff] border-b">
                    <div className="flex items-center justify-between px-4 py-2">
                        <NavButton to={mode === "saved" 
                            ? "/saved" 
                            : mode === "home"
                            ? "/"
                            : "/recipes"}>
                            <ArrowLeft className="w-4 h-4" />
                        </NavButton>

                        { mode === "saved" && (
                            <div className="flex items-center gap-4">
                                <button
                                    onClick={() => navigate(`${location.pathname}/edit`)}
                                    className="px-3 py-2 rounded-md border"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={handleDelete}
                                    className="bg-red-500 text-white px-3 py-2 rounded-md border"
                                >
                                    Delete
                                </button>
                            </div>
                        )}

                        { mode === "public" && (
                            <div className="flex items-center gap-4">
                                <button
                                    onClick={handleSave}
                                    className="bg-[#000000] text-white px-3 py-2 rounded-md border"
                                >
                                    Save
                                </button>
                            </div>
                        )}

                        
                    </div>
                </div>

                {/* Image */}
                <div className="w-full h-64 md:h-96 relative overflow-hidden bg-[#ececf0] -mt-4 lg:rounded-xl">
                    <img
                        src={data.image ? `${config.imgSrc}${data.image}` : "default.png"}
                        alt={data.title}
                        className="w-full h-full object-cover"
                    />
                </div>

                <div className="mx-auto px-4 -mt-8 relative z-1">
                    <div className="bg-[#ffffff] flex flex-col gap-6 rounded-xl border p-6 md:p-8 shadow-lg">
                        {/* Title and desc */}
                        <div className="mb-4">
                            <h2 className="text-3xl md:text-4xl mb-4">
                                {data.title}
                            </h2>
                            {data.description && (
                                <p className="text-[#717182] text-lg">
                                    {data.description
                                        .split('\n')
                                        .filter(line => line.trim())
                                        .map((line, index) => (
                                        <div key={index} className="flex gap-4 last:mb-0 p-3 rounded-md">
                                            {line}
                                        </div>
                                    ))}
                                </p>
                            )}
                        </div>

                        {/* Tags */}
                        {data.tags && data.tags.length > 0 && (
                            <div className="flex flex-wrap gap-2 mb-6">
                                {data.tags.map((tag) => (
                                    <TagComponent label={tag} />
                                ))}
                            </div>
                        )}

                        {/* Stats */}
                        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
                            <InfoCard text="Prep" val={`${data.prepTime ?? 0} min`} icon={Clock} color="#030213"/>
                            <InfoCard text="Cook" val={`${data.cookTime ?? 0} min`} icon={ChefHat} color="#ffa600"/>
                            <InfoCard text="Servings" 
                                val={`${servings}`}
                                icon={Users}
                                color="#00ff2f"
                                onIncrease={() => setServings(s => s + 1)}
                                onDecrease={() => setServings(s => Math.max(1, s - 1))}
                            />
                            <InfoCard text="Calories" val={`${data.calories ?? 0}`} icon={Flame} color="#ff0d00"/>
                        </div>
                        
                        {/* Ingredients */}
                        <div className="mb-8">
                            <h3 className="mb-4 flex items-center gap-2">
                                <span>Ingredients - </span>
                                <span className="text-xl bg-[#f3f3f3] rounded-lg px-2">{data.ingredients.length}</span>
                            </h3>
                            
                            <div className="bg-[#eeeeee] rounded-lg p-6">
                                <ul className="space-y-3">
                                    {scaledIngredients?.map((ingredient, index) => (
                                        <li
                                            key={index}
                                            className="flex items-start gap-3 p-3 rounded-md hover:bg-[#ffffff] transition-colors"
                                        >
                                            <div className="w-2 h-2 rounded-full bg-[#000000] mt-2 flex-shrink-0" />
                                            <div className="flex-1">
                                                <span className="font-medium">{ingredient.name}</span>
                                                <span className="text-[#717182] ml-2">
                                                    {(ingredient.quantity > 0 || ingredient.unit !== "") && "-"} {ingredient.quantity > 0 && formatQuantity(ingredient.quantity)} {ingredient.unit && ingredient.unit}
                                                </span>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>

                        {/* Instructions */}
                        {data.instructions && (
                            <div className="mb-6">
                                <h3 className="mb-4">Instructions</h3>
  
                                <div className="bg-[#eeeeee] rounded-lg p-6">
                                    {data.instructions
                                        .split('\n')
                                        .filter(line => line.trim())
                                        .map((line, index) => (
                                        <div key={index} className="flex gap-4 mb-2 last:mb-0 p-3 rounded-md hover:bg-[#ffffff] transition-colors">
                                            
                                            <div className="w-8 h-8 rounded-full bg-[#030213] text-[#e4e4e4] flex items-center justify-center flex-shrink-0 font-semibold text-sm">
                                                {index + 1}
                                            </div>

                                            <p className="flex-1 pt-1">{line}</p>
                                        </div>
                                    ))}
                                </div>

                            </div>
                        )}

                        {/* Meta data */}
                        <div className="border-b" />
                        <div className="text-xs text-[#717182] flex flex-wrap gap-4">
                            <span>Created at: {data.createdAt ? new Date(data.createdAt).toLocaleString() : "None"}</span>
                            <span>Modified at: {data.updatedAt ? new Date(data.updatedAt).toLocaleString() : "None"}</span>
                        </div>


                    </div>
                </div>

                {/* <pre className="whitespace-pre-wrap break-words max-w-full">
                    {JSON.stringify(data, null, 2)}
                </pre> */}
                
                
                
            </div>
            
        </>
        

    )
}

export default SavedRecipe;
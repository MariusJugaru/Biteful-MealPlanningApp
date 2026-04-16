import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Header from "../Components/Header";
import NavButton from "../Components/NavButton";

import { ArrowLeft, Tag, Clock, ChefHat, Flame, Users } from "lucide-react";
import InfoCard from "../Components/InfoCard";

type Ingredient = {
    name: string;
    quantity: number;
    unit?: string;
}

type Recipe = {
    title: string;
    description: string | null;
    ingredients: Ingredient[];
    instructions: string | null;
    tags: string[] | null;
    prepTime: number | null;
    cookTime: number | null;
    servings: number | null;
    calories: number | null;
    image: string | null;
    visiblity: "private" | "public";
    createdAt: string | null;
    updatedAt: string | null;
}

function SavedRecipe() {
    const { id } = useParams();
    const [data, setData] = useState<Recipe | null>(null);

    useEffect(() => {
        const fetchRecipe = async () => {
            const token = localStorage.getItem("token");

            const response = await fetch(`http://localhost:8081/api/recipes/me/${id}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            const result = await response.json();
            setData(result);

        };

        fetchRecipe();
    }, [id]);

    if (!data) return <div>Loading...</div>;

    return(
        <>
            <Header />
            <div className="min-h-screen bg-[#ffffff] pb-8 lg:px-24">
                <div className="border-x">
                    {/* Header with return button */}
                    <div className="sticky top-0 z-2 bg-[#ffffff] border-b">
                        <div className="flex items-center justify-between px-4 py-2">
                            <NavButton to="/saved">
                                <ArrowLeft className="w-4 h-4" />
                            </NavButton>
                            Edit
                            Delete
                        </div>
                    </div>

                    {/* Image */}
                    <div className="w-full h-64 md:h-96 relative overflow-hidden bg-[#ececf0]">
                        <img
                            src={data?.image ?? "default.png"}
                            alt={data.title}
                            className="w-full h-full object-cover"
                        />
                    </div>

                    <div className="mx-auto px-4 -mt-8 relative z-1">
                        <div className="bg-[#ffffff] text-[#000000] flex flex-col gap-6 rounded-xl border p-6 md:p-8 shadow-lg">
                            {/* Title and desc */}
                            <div className="mb-4">
                                <h2 className="text-3xl md:text-4xl mb-4">
                                    {data.title}
                                </h2>
                                {data.description && (
                                    <p className="text-[#717182] text-lg">
                                        {data.description}
                                    </p>
                                )}
                            </div>

                            {/* Tags */}
                            {data.tags && data.tags.length > 0 && (
                                <div className="flex flex-wrap gap-2 mb-6">
                                    {data.tags.map((tag) => (
                                        <div className="inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-xs">
                                            <Tag className="w-3 h-3" />
                                            <div>{tag}</div>
                                        </div>
                                        
                                    ))}
                                </div>
                            )}

                            {/* Stats */}
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
                                <InfoCard text="Prep" val={`${data.prepTime ?? 0} min`} icon={Clock} color="#030213"/>
                                <InfoCard text="Cook" val={`${data.cookTime ?? 0} min`} icon={ChefHat} color="#ffa600"/>
                                <InfoCard text="Servings" val={`${data.servings ?? 0}`} icon={Users} color="#00ff2f"/>
                                <InfoCard text="Calories" val={`${data.calories ?? 0}`} icon={Flame} color="#ff0d00"/>
                            </div>
                        </div>
                    </div>

                    <pre className="whitespace-pre-wrap break-words max-w-full">
                        {JSON.stringify(data, null, 2)}
                    </pre>
                </div>
                
                
            </div>
            
        </>
        

    )
}

export default SavedRecipe;
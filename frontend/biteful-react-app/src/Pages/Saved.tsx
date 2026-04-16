import { useEffect, useState } from "react";
import Header from "../Components/Header";
import RecipeCard from "../Components/RecipeCard";
import NavButton from "../Components/NavButton";

type Recipe = {
    id: string;
    title: string;
    image: string;
};

type ApiResponse = {
    content: Recipe[];
}

function Saved() {
    console.log("Recipes rendered");
    const [data, setData] = useState<ApiResponse | null>(null);

    useEffect(() => {
        const fetchRecipes = async () => {
            const token = localStorage.getItem("token");

            const response = await fetch("http://localhost:8081/api/recipes/me", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            const result = await response.json();
            setData(result);
        };

        fetchRecipes();
    }, []);

    
    return(
        <>
            <Header />
            {data?.content? (
                <pre>
                    {/* {JSON.stringify(data.content)} */}
                    <div className="mx-auto px-6 py-6">
                        <div className="flex justify-between items-center">
                            <h3>Saved Recipes</h3>
                            <NavButton to="/saved/add" variant="gray">
                                Add Recipe
                            </NavButton>
                        </div>
                        
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 py-6">
                            {data?.content?.map((recipe) => (
                                <RecipeCard key={recipe.id} recipe={recipe} />
                            ))}
                        </div>
                    </div>
                    
                </pre>
            ) : (
                <p>Loading...</p>
            )}
                
            
        </>
    );
}

export default Saved;
import Header from "../Components/Header";
import RecipeCard from "../Components/RecipeCard";
import NavButton from "../Components/NavButton";
import useFetch from "../Hooks/useFetch";
import { config } from "../config";

type Recipe = {
    id: string;
    title: string;
    image: string;
};

type ApiResponse = {
    content: Recipe[];
}

function Saved() {
    const token = localStorage.getItem("token");
    
    const { data, loading, error } = useFetch<ApiResponse>(
        `${config.apiUrl}/api/recipes/me`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    )

    if (error) {
        return <div>{error}</div>;
    }

    return(
        <>
            <Header />
  
            <div className="mx-auto px-6 py-6">
                {/* In page header */}
                <div className="flex justify-between items-center">
                    <h3>Saved Recipes</h3>
                    <NavButton to="/saved/add" variant="gray">
                        Add Recipe
                    </NavButton>
                </div>
                
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    // Recipe cards
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 py-6">
                        {data?.content?.map((recipe) => (
                            <RecipeCard key={recipe.id} recipe={recipe} />
                        ))}
                    </div>
                )}
            </div>
 
            {/* {!loading &&
                <pre className="whitespace-pre-wrap break-words max-w-full">
                    {JSON.stringify(data.content, null, 2)}
                </pre>
            } */}
            
            
        </>
    );
}

export default Saved;
import Header from "../Components/Header";
import RecipeCard from "../Components/RecipeCard";
import NavButton from "../Components/NavButton";
import { config } from "../config";
import { Sparkles } from "lucide-react";
import AiRecipeModal from "../Components/AiRecipeModal";
import { useEffect, useRef, useState } from "react";

import { useLocation } from "react-router-dom";

type Recipe = {
    id: string;
    title: string;
    image: string;
};

type ApiResponse = {
    content: Recipe[];
}

function Recipes({ mode = "saved" } : { mode?: "saved" | "public"}) {
    const token = localStorage.getItem("token");
    
    const [aiOpen, setAiOpen] = useState(false);

    const api =
        mode === "saved"
            ? "/api/recipes/me"
            : "/api/recipes/public";

    const [recipes, setRecipes] = useState<Recipe[]>([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);

    const observerRef = useRef<HTMLDivElement>(null);
    
    const fetchingRef = useRef(false);
    const location = useLocation();

    const [resetKey, setResetKey] = useState(0);

    useEffect(() => {
        setRecipes([]);
        setPage(0);
        setHasMore(true);
        fetchingRef.current = false;
        setResetKey(k => k + 1);
    }, [location.pathname]);

    useEffect(() => {
        async function loadRecipes() {
            if (!hasMore || fetchingRef.current) return;

            fetchingRef.current = true;
            setLoading(true);

            try {
                const response = await fetch(
                    `${config.apiUrl}${api}?page=${page}&size=18`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json",
                        },
                    }
                );

                const data: ApiResponse = await response.json();

                setRecipes(prev => {
                    const existingIds = new Set(prev.map(r => r.id));

                    const filtered = data.content.filter(
                        r => !existingIds.has(r.id)
                    );

                    return [...prev, ...filtered];
                });

                if (data.content.length < 18) {
                    setHasMore(false);
                }

            } finally {
                fetchingRef.current = false;
                setLoading(false);
            }
        }

        loadRecipes();
    }, [page, api, resetKey]);

    useEffect(() => {
        const observer = new IntersectionObserver(
            entries => {
                if (
                    entries[0].isIntersecting &&
                    hasMore &&
                    !fetchingRef.current
                ) {
                    setPage(prev => prev + 1);
                }
            },
            {
                rootMargin: "300px",
                threshold: 0,
            }
        );

        const current = observerRef.current;

        if (current) {
            observer.observe(current);
        }

        return () => {
            if (current) {
                observer.unobserve(current);
            }
        };

    }, [hasMore]);

    return(
        <>
            <Header />
  
            <div className="mx-auto px-6 py-6">
                {/* In page header */}
                <div className="flex justify-between items-center">
                    <h3>
                        {mode === "saved" ? "Saved Recipes" : "Recipes"}
                    </h3>

                    {/* Add recipe button */}
                    {mode === "saved" && (
                        <div className="flex items-center gap-2">
                            <button
                                onClick={() => setAiOpen(true)}
                                className="flex items-center gap-2 bg-black text-white px-3 py-2 rounded-md text-sm font-medium"
                            >
                                <Sparkles className="w-4 h-4" />
                                AI Recipe
                            </button>
                            <NavButton to="/saved/add" variant="gray">
                                Add Recipe
                            </NavButton>
                        </div>
                    )}
                </div>

                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 py-6">
                    {recipes.map(recipe => (
                        <RecipeCard
                            key={recipe.id}
                            recipe={recipe}
                            mode={mode}
                        />
                    ))}
                </div>
                <div ref={observerRef} className="h-10" />

                {loading && (
                    <p className="text-center py-4">
                        Loading more recipes...
                    </p>
                )}
            </div>
            
            <AiRecipeModal
                open={aiOpen}
                onClose={() => setAiOpen(false)}
            />
        </>
    );
}

export default Recipes;
import useFetch from "../Hooks/useFetch";
import { config } from "../config";
import RecipeCard from "./RecipeCard";


type AddMealProps = {
    open: boolean;
    onClose: () => void;
    type: "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK";
    date: string;
    onSelectRecipe: (recipeId: string) => void;
}

type Recipe = {
    id: string;
    title: string;
    image: string;
};

type ApiResponse = {
    content: Recipe[];
}

function formatMealType(type: string) {
    return type.charAt(0) + type.slice(1).toLowerCase();
}

function getDayName(dateString: string) {
    const date = new Date(dateString);

    return date.toLocaleDateString("en-US", {
        weekday: "long",
    });
}


function AddMealModal({ open, onClose, type, date, onSelectRecipe } : AddMealProps) {

    // const [selectedCategory, setSelectedCategory] = useState<string>(type);
    // const [searchQuery, setSearchQuery] = useState("");
    // const [isMobile, setIsMobile] = useState(false);

    // useEffect(() => {
    //     const checkMobile = () => {
    //         setIsMobile(window.innerWidth < 768);
    //     };

    //     checkMobile();
    //     window.addEventListener("resize", checkMobile);

    //     return () => window.removeEventListener("resize", checkMobile);
    // }, []);

    // useEffect(() => {
    //     if (open) {
    //         setSelectedCategory(type);
    //         setSearchQuery("");
    //     }
    // }, [open, type]);

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

    if (!open) return null;
    if (error) {
        return <div>{error}</div>;
    }

    return(
         <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            
            <div className="bg-white w-full max-w-4xl rounded-xl p-4">
                
                <div className="flex justify-between items-center mb-4 px-2">
                    <h3>
                        Add {formatMealType(type)} for {getDayName(date)}
                    </h3>

                    <button className="" onClick={onClose}>
                        ✕
                    </button>
                </div>

                <div className="flex-1 overflow-y-auto px-4 pb-4 max-h-[60vh]">
            
                    {loading ? (
                        <p className="text-center text-gray-400">
                            Loading...
                        </p>
                    ) : (
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                            {data?.content?.map(recipe => (
                                <RecipeCard
                                    key={recipe.id}
                                    recipe={recipe}
                                    onClick={(recipe) => {
                                        onSelectRecipe(recipe.id);
                                        onClose();
                                    }}
                                />
                            ))}
                        </div>
                    )}

                </div>

            </div>

        </div>
    );
}

export default AddMealModal;
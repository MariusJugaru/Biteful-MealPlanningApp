import { useNavigate } from "react-router-dom";
import { config } from "../config";

type Recipe = {
    id: string;
    title: string;
    image: string;
};

type RecipeCardProps = {
    recipe: Recipe;
    mode?: "saved" | "public";
    onClick?: (recipe: Recipe) => void;
};

function RecipeCard({ recipe, mode = "saved", onClick } : RecipeCardProps) {
    const navigate = useNavigate();

    const api =
        mode === "saved"
            ? "/saved/"
            : "/recipes/";

    function handleClick() {
        if (onClick) {
            onClick(recipe);
        } else {
            navigate(`${api}${recipe.id}`);
        }
    }

    return (
        <div
            onClick={handleClick}
            className="cursor-pointer rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition duration-200 bg-white"
        >
            <img
                src={`${config.imgSrc}${recipe.image}`}
                alt={recipe.title}
                className="w-full h-80 md:h-60 lg:h-40 object-cover"
            />

            <div className="p-4">
                <h3 className="text-lg font-semibold text-gray-800">
                    {recipe.title}
                </h3>
            </div>
        </div>
    )
}

export default RecipeCard;
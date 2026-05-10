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
};

function RecipeCard({ recipe, mode = "saved" } : RecipeCardProps) {
    const navigate = useNavigate();

    const api =
        mode === "saved"
            ? "/saved/"
            : "/recipes/";

    return (
        <div
            onClick={() => navigate(`${api}${recipe.id}`)}
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
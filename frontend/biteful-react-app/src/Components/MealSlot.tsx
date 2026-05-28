import { X, Plus } from "lucide-react";
import { config } from "../config";


type MealSlotProps = {
    mealType: string;
    recipe?: {
        recipeId?: string;
        title?: string;
        calories?: number;
        image?: string | null;
    };
    onAdd?: () => void;
    onRemove?: () => void;
}

export function MealSlot({ mealType, recipe, onAdd, onRemove }: MealSlotProps) {
    const hasRecipe = !!recipe;

    return (
        <div
            className={`overflow-hidden rounded-xl border flex flex-col 
                ${hasRecipe ? "bg-white" : "bg-[#f6f6f8]"}`
            }
        >
            <div className="p-3 flex items-start justify-between">
                <span className="text-sm text-[#717182]">{mealType}</span>

                {hasRecipe && (
                    <button
                        onClick={onRemove}
                        className="h-6 w-6 -mt-1 -mr-1 inline-flex items-center justify-center rounded-md hover:bg-[#e9ebef]"
                    >
                        <X className="w-4 h-4" />
                    </button>
                )}
            </div>

            {hasRecipe ? (
                <>
                    <div className="relative h-32 overflow-hidden">
                        <img
                            src={`${config.imgSrc}${recipe?.image || ""}`}
                            alt={recipe?.title || ""}
                            className="w-full h-full object-cover"
                        />
                    </div>

                    <div className="p-3">
                        <p className="text-sm mb-1 line-clamp-2">{recipe?.title}</p>
                        <p className="text-xs text-[#717182]">
                            {recipe?.calories ?? 0} cal
                        </p>
                    </div>
                </>
                ) : (
                <div className="p-3 pt-0">
                    <button
                        onClick={onAdd}
                        className="w-full flex items-center gap-2 p-2 text-sm text-[#717182] hover:text-[#3b3b44] hover:bg-[#e9ebef] transition rounded-md"
                    >
                        <Plus className="w-4 h-4" />
                        Add meal
                    </button>
                </div>
            )}
        </div>
    );
}

export default MealSlot;
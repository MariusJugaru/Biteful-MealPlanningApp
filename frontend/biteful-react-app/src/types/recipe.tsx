import type { Ingredient } from "../Pages/SavedRecipe";

export type Recipe = {
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
    visibility: "private" | "public";
    createdAt: string | null;
    updatedAt: string | null;
};

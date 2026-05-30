
export type MealResponse = {
    recipeId: string;
    title: string;
    type: "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK";
    date: string;
    image: string | null;
    calories: number;
};

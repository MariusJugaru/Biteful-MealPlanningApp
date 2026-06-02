import { useEffect, useState } from "react";
import useFetch from "../Hooks/useFetch";
import { config } from "../config";
import RecipeCard from "./RecipeCard";

import { DateRange } from "react-date-range";
import { BookOpen, ChefHat, Cookie, Salad, Save, Target } from "lucide-react";

import type { MealResponse } from "../types/mealsResponse";

type GeneratePlanProps = {
    open: boolean;
    onClose: () => void;
    onGenerated: (meals: MealResponse[]) => void;
}

function GeneratePlanModal({ open, onClose, onGenerated } : GeneratePlanProps) {
    const [dailyCalorieGoal, setDailyCalorieGoal] = useState(2000);
    const [mealPrepFrequency, setMealPrepFrequency] = useState(1);

    const frequencyLabels = ["Once per plan", "Twice per plan", "3 times per plan", "4 times per plan", "Daily"];
    const frequencyLabel = frequencyLabels[mealPrepFrequency - 1] || "Twice per plan";

    const [usePublicRecipes, setUsePublicRecipes] = useState(true);
    const [useSavedRecipes, setUseSavedRecipes] = useState(true);
    const [includeSnacks, setIncludeSnacks] = useState(false);

    const [range, setRange] = useState([
        {
            startDate: new Date(),
            endDate: new Date(),
            key: "selection",
        },
    ]);

    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const checkMobile = () => {
            setIsMobile(window.innerWidth < 768);
        };

        checkMobile();
        window.addEventListener("resize", checkMobile);

        return () => window.removeEventListener("resize", checkMobile);
    }, []);

    const token = localStorage.getItem("token");

    const formatDate = (date: Date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    };

    const handleGenerate = async () => {
        const startDate = range[0].startDate!;
        const endDate = range[0].endDate!;

        const days =
            Math.floor(
                (endDate.getTime() - startDate.getTime()) /
                (1000 * 60 * 60 * 24)
            ) + 1;

        const payload = {
            days,
            startDate: formatDate(startDate),
            cookingTimesPerPlan: mealPrepFrequency,
            hasPublic: usePublicRecipes,
            hasPrivate: useSavedRecipes,
            hasSnack: includeSnacks,
            caloriesObjective: dailyCalorieGoal,
        };

        const response = await fetch(`${config.apiUrl}/api/plans/generate`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        console.log(payload);

        if (!response.ok) {
            console.error("Generate plan failed");
            return;
        }

        const data = await response.json();

        console.log(data);
        onGenerated(data);

        onClose();
    };

    if (!open) return null;

    return(
         <div className="select-none fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            
            <div className="bg-white w-full max-w-3xl rounded-xl p-4">
                
                <div className="flex justify-between items-center mb-4 px-2">
                    <h2 className="text-center w-full">
                        Plan your meal
                    </h2>

                    <button className="" onClick={onClose}>
                        ✕
                    </button>
                </div>

                {/* Scroll */}
                <div className="flex-1 overflow-y-auto text-center w-full-2 max-h-[75vh] ml-2">

                    <div className="px-4 mb-6">
                        <div className="border rounded-md mb-5">
                            <h4 className="mt-2">Date range</h4>
                            <DateRange
                                ranges={range}
                                onChange={(item: any) => setRange([item.selection])}
                                moveRangeOnFirstSelection={false}
                                months={isMobile ? 1 : 2}
                                direction="horizontal"
                                className="py-5"
                            />
                        </div>

                        <div className="border-b mb-4"></div>

                        {/* Daily Calories */}
                        <div className="flex items-center gap-2 mb-2">
                            <Target className="w-4 h-4" />
                            <div className="font-semibold">Daily Calorie Goal</div>
                        </div>
                        <div className="space-y-2 mb-4">
                            <input
                                type="number"
                                value={dailyCalorieGoal}
                                onChange={(e) => setDailyCalorieGoal(Number(e.target.value))}
                                min={800}
                                max={4000}
                                step={50}
                                className="w-full text-center text-lg font-semibold border rounded-md p-2 bg-[#ececec]"
                            />

                            <input
                                type="range"
                                min={800}
                                max={4000}
                                step={50}
                                value={dailyCalorieGoal}
                                onChange={(e) => setDailyCalorieGoal(Number(e.target.value))}
                                className="w-full accent-black cursor-pointer"
                            />

                            <div className="flex justify-between text-xs text-gray-500">
                                <span>800 cal</span>
                                <span>4000 cal</span>
                            </div>
                        </div>

                        <div className="border-b mb-4"></div>

                        {/* Meal Prep Frequency */}
                        <div className="flex items-center gap-2 mb-2">
                            <ChefHat className="w-4 h-4" />
                            <div className="font-semibold">Meal Prep Frequency</div>
                        </div>
                        <span className="px-3 py-2 bg-[#e9e9e9] rounded-xl">{frequencyLabel}</span>
                        <div className="mb-4 mt-4">
                            <input
                                type="range"
                                min={1}
                                max={5}
                                step={1}
                                value={mealPrepFrequency}
                                onChange={(e) => setMealPrepFrequency(Number(e.target.value))}
                                className="w-full accent-black cursor-pointer"
                            />

                            <div className="flex justify-between text-xs text-gray-500">
                                <span>1x</span>
                                <span>2x</span>
                                <span>3x</span>
                                <span>4x</span>
                                <span>Daily</span>
                            </div>
                            <p className="text-xs text-muted-foreground text-center mt-2">
                                {mealPrepFrequency === 1 && "Cook once, eat all week - batch cooking"}
                                {mealPrepFrequency === 2 && "Cook twice per plan - split prep sessions"}
                                {mealPrepFrequency === 3 && "Cook 3 times per plan - diversity prep"}
                                {mealPrepFrequency === 4 && "Cook 4 times per plan - frequent fresh meals"}
                                {mealPrepFrequency === 5 && "Cook daily - always fresh meals"}
                            </p>
                        </div>

                        <div className="border-b mb-4"></div>

                        {/* Recipe sources */}
                        <div className="flex items-center gap-2 mb-2">
                            <Salad className="w-4 h-4" />
                            <div className="font-semibold">Recipe Sources</div>
                        </div>

                        {/* Public recipes */}
                        <div className="flex items-center justify-between p-3 rounded-lg border bg-card mb-2">
                            <div className="flex items-start gap-3">
                                <BookOpen className="w-4 h-4 text-primary mt-2 flex-shrink-0" />

                                <div className="text-left">
                                    <div className="font-medium leading-none">
                                        Public Recipes
                                    </div>

                                    <div className="text-xs text-muted-foreground mt-1">
                                        Use recipes from the community library
                                    </div>
                                </div>
                            </div>
                            <label className="relative inline-flex items-center cursor-pointer">
                                <input
                                    type="checkbox"
                                    checked={usePublicRecipes}
                                    onChange={(e) => setUsePublicRecipes(e.target.checked)}
                                    className="sr-only"
                                />

                                <div
                                    className={`w-11 h-6 rounded-full transition-colors ${
                                        usePublicRecipes ? "bg-black" : "bg-gray-300"
                                    }`}
                                >
                                    <div
                                        className={`w-5 h-5 bg-white rounded-full mt-0.5 transition-transform ${
                                            usePublicRecipes
                                                ? "translate-x-5"
                                                : "translate-x-0.5"
                                        }`}
                                    />
                                </div>
                            </label>
                        </div>

                        {/* Saved recipes */}
                        <div className="flex items-center justify-between p-3 rounded-lg border bg-card mb-2">
                            <div className="flex items-start gap-3">
                                <Save className="w-4 h-4 text-primary mt-2 flex-shrink-0" />

                                <div className="text-left">
                                    <div className="font-medium leading-none">
                                        Saved Recipes
                                    </div>

                                    <div className="text-xs text-muted-foreground mt-1">
                                        Use your personal saved recipes
                                    </div>
                                </div>
                            </div>
                            <label className="relative inline-flex items-center cursor-pointer">
                                <input
                                    type="checkbox"
                                    checked={useSavedRecipes}
                                    onChange={(e) => setUseSavedRecipes(e.target.checked)}
                                    className="sr-only"
                                />

                                <div
                                    className={`w-11 h-6 rounded-full transition-colors ${
                                        useSavedRecipes ? "bg-black" : "bg-gray-300"
                                    }`}
                                >
                                    <div
                                        className={`w-5 h-5 bg-white rounded-full mt-0.5 transition-transform ${
                                            useSavedRecipes
                                                ? "translate-x-5"
                                                : "translate-x-0.5"
                                        }`}
                                    />
                                </div>
                            </label>
                        </div>

                        {/* Snacks */}
                        <div className="flex items-center justify-between p-3 rounded-lg border bg-card">
                            <div className="flex items-start gap-3">
                                <Cookie className="w-4 h-4 text-primary mt-2 flex-shrink-0" />

                                <div className="text-left">
                                    <div className="font-medium leading-none">
                                        Include Snacks
                                    </div>

                                    <div className="text-xs text-muted-foreground mt-1">
                                        Add snacks between meals
                                    </div>
                                </div>
                            </div>
                            <label className="relative inline-flex items-center cursor-pointer">
                                <input
                                    type="checkbox"
                                    checked={includeSnacks}
                                    onChange={(e) => setIncludeSnacks(e.target.checked)}
                                    className="sr-only"
                                />

                                <div
                                    className={`w-11 h-6 rounded-full transition-colors ${
                                        includeSnacks ? "bg-black" : "bg-gray-300"
                                    }`}
                                >
                                    <div
                                        className={`w-5 h-5 bg-white rounded-full mt-0.5 transition-transform ${
                                            includeSnacks
                                                ? "translate-x-5"
                                                : "translate-x-0.5"
                                        }`}
                                    />
                                </div>
                            </label>
                        </div>

                            

                    </div>
                    

                    
                </div>
                <div className="border-b mb-4"></div>

                <div className="flex gap-3 justify-end">
                    <button
                        onClick={onClose}
                        className="flex items-center gap-2 border px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleGenerate}
                        className="flex items-center gap-2 bg-gray-400 text-white border px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Generate
                    </button>
                </div>

            </div>

        </div>
    );
}

export default GeneratePlanModal;
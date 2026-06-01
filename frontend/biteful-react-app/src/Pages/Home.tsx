import { Link } from "react-router-dom";

import Header from "../Components/Header";
import StatsSection from "../Components/StatsSection";
import MealSlot from "../Components/MealSlot";
import { useEffect, useState } from "react";
import { config } from "../config";
import AddMealModal from "../Components/AddMealModal";
import { ChevronLeft, ChevronRight } from "lucide-react";
import GeneratePlanModal from "../Components/GeneratePlanModal";
import type { MealResponse } from "../types/mealsResponse";

const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

function getWeekBounds(baseDate: Date) {

    const monday = new Date(baseDate);
    const day = monday.getDay();

    const diff = day === 0 ? -6 : 1 - day;

    monday.setDate(baseDate.getDate() + diff);

    const sunday = new Date(monday);
    sunday.setDate(monday.getDate() + 6);

    const format = (date: Date) =>
        date.toISOString().split("T")[0];

    return {
        startDate: format(monday),
        endDate: format(sunday),
        dates: Array.from({ length: 7 }, (_, i) => {
            const d = new Date(monday);
            d.setDate(monday.getDate() + i);

            return {
                label: d.toLocaleDateString("en-US", {
                    weekday: "long",
                }),
                date: format(d),
            };
        }),
    };
}

function Home() {
    const token = localStorage.getItem("token");

    // Get week interval
    const [currentWeekDate, setCurrentWeekDate] = useState(new Date());
    const { startDate, endDate, dates } = getWeekBounds(currentWeekDate);

    const previousWeek = () => {
        setCurrentWeekDate(prev => {
            const d = new Date(prev);
            d.setDate(d.getDate() - 7);
            return d;
        });
    };

    const nextWeek = () => {
        setCurrentWeekDate(prev => {
            const d = new Date(prev);
            d.setDate(d.getDate() + 7);
            return d;
        });
    };

    // Get meals
    const [meals, setMeals] = useState<MealResponse[]>([]);
    
    useEffect(() => {    
        async function loadMeals() {
            const response = await fetch(`${config.apiUrl}/api/plans?startDate=${startDate}&endDate=${endDate}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                }
            });

            const data = await response.json();

            setMeals(data);
        }

        loadMeals();
    }, [startDate, endDate]);

    // Generate meal plan modal
    const [generatePlanModal, setGeneratePlanModal] = useState<{
        open: boolean;
    }>({
        open: false,
    });

    const [generatedMeals, setGeneratedMeals] = useState<MealResponse[]>([]);

    function getMeals() {
        if (generatedMeals.length > 0) 
            return generatedMeals;
        return meals;
    }

    const getMeal = (
        date: string,
        type: string
    ) => {
        return getMeals().find(
            meal =>
                meal.date === date &&
                meal.type === type
        );
    };

    const displayedMeals =
        generatedMeals.length > 0
            ? generatedMeals
            : meals;

    const currentWeekMeals = displayedMeals.filter(
        meal =>
            meal.date >= startDate &&
            meal.date <= endDate
    );

    // Modify meal plan
    const [addMealModal, setAddMealModal] = useState<{
        open: boolean;
        day: string;
        type: "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK";
    }>({
        open: false,
        day: "",
        type: "BREAKFAST",
    });
    
    function openAddMeal(date: string, type: MealResponse["type"]) {
        setAddMealModal({
            open: true,
            day: date,
            type
        });
    }

    async function addMeal(recipeId: string) {
        
        const response = await fetch(`${config.apiUrl}/api/plans`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                date: addMealModal.day,
                type: addMealModal.type,
                recipeId,
            }),
        });

        if (!response.ok) {
            console.error("Add meal failed");
            return;
        }

        const newMeal: MealResponse = await response.json();

        setMeals(prev => [...prev, newMeal]);

        setAddMealModal(prev => ({
            ...prev,
            open: false,
        }));
    }

    async function removeMeal(
        date: string,
        type: string
    ) {
        if (generatedMeals.length > 0) {
            setGeneratedMeals(prev =>
                prev.filter(
                    meal =>
                        !(meal.date === date && meal.type === type)
                )
            );
            return;
        }

        const response = await fetch(
            `${config.apiUrl}/api/plans`,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    date,
                    type
                })
            }
        )

        if (!response.ok) {
            console.error("Delete failed");
            return;
        }

        setMeals(prev =>
            prev.filter(
                meal =>
                    !(
                        meal.date === date &&
                        meal.type === type
                    )
            )
        );
    }

    async function saveGeneratedPlan(
        generatedPlan: MealResponse[],
    ) {
        console.log(JSON.stringify(generatedMeals))

        const response = await fetch(
            `${config.apiUrl}/api/plans/save`,
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(generatedMeals)
            }
        )

        

        if (!response.ok) {
            console.error("Save failed");
            return;
        }

        setMeals(prev => {
            const generatedKeys = new Set(
                generatedMeals.map(
                    meal => `${meal.date}-${meal.type}`
                )
            );

            return [
                ...prev.filter(
                    meal =>
                        !generatedKeys.has(
                            `${meal.date}-${meal.type}`
                        )
                ),
                ...generatedMeals
            ];
        });
        setGeneratedMeals([]);
    }

    return (
        <>
            <Header />

            <div className="mx-auto px-12 py-8 select-none">

                {/* Weekly Calories and Daily Avg. */}
                <StatsSection
                    totalWeeklyCalories={
                        currentWeekMeals.reduce(
                            (sum, meal) =>
                                sum + meal.calories,
                            0
                        )
                    }
                />

                <div className="flex items-center justify-between flex-wrap gap-4">
                    <div className="flex items-center gap-4">

                        {/* Header */}
                        <h3 className="mt-4 mb-4">Weekly Meal Plan</h3>

                        {/* Week picker */}
                        <div className="flex items-center gap-2">
                            <div 
                                className="rounded-md border p-1.5 hover:bg-[#ececec] active:bg-[#dbdbdb] transition" 
                                onClick={previousWeek}
                            >
                                <ChevronLeft className="w-4 h-4"/>
                            </div>
                            <div className="rounded-md border px-2 py-1 text-xs font-semibold cursor-default">{startDate} - {endDate}</div>
                            <div
                                className="rounded-md border p-1.5 hover:bg-[#ececec] active:bg-[#dbdbdb] transition cursor-pointer"
                                onClick={nextWeek}
                            >
                                <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>
                       
                    </div>

                    <div className="flex items-center gap-2">
                        {/* Save Generated Plan Button */}
                        {generatedMeals.length > 0 && (
                            <button
                                onClick={() => saveGeneratedPlan(generatedMeals)}
                                className="flex items-center gap-2 bg-green-400 text-white px-3 py-2 rounded-md text-sm font-medium"
                            >
                                Save
                            </button>
                        )}

                        {/* Cancel Generated Plan Button */}
                        {generatedMeals.length > 0 && (
                            <button
                                onClick={() => setGeneratedMeals([])}
                                className="flex items-center gap-2 bg-gray-200 text-black border px-3 py-2 rounded-md text-sm font-medium"
                            >
                                Cancel
                            </button>
                        )}

                        {/* Generate Plan Modal Button */}
                        <button
                            onClick={() => setGeneratePlanModal({ open: true })}
                            className="flex items-center gap-2 bg-black text-white px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Generate Plan
                        </button>
                        
                    </div>
                    
                </div>

                <div className="hidden lg:grid lg:grid-cols-7 gap-4">
                    {/* Load Meals */}
                    {dates.map(day => (
                        <div
                            key={day.date}
                            className="space-y-3"
                        >
                            <h3 className="pb-2 border-b">
                                {day.label}
                            </h3>

                            <MealSlot
                                mealType="BREAKFAST"
                                recipe={getMeal(day.date, "BREAKFAST")}
                                onAdd={() => openAddMeal(day.date, "BREAKFAST")}
                                onRemove={(date, mealType) =>
                                    removeMeal(date, mealType)
                                }
                            />

                            <MealSlot
                                mealType="LUNCH"
                                recipe={getMeal(day.date, "LUNCH")}
                                onAdd={() => openAddMeal(day.date, "LUNCH")}
                                onRemove={(date, mealType) =>
                                    removeMeal(date, mealType)
                                }
                            />

                            <MealSlot
                                mealType="DINNER"
                                recipe={getMeal(day.date, "DINNER")}
                                onAdd={() => openAddMeal(day.date, "DINNER")}
                                onRemove={(date, mealType) =>
                                    removeMeal(date, mealType)
                                }
                            />

                            <MealSlot
                                mealType="SNACK"
                                recipe={getMeal(day.date, "SNACK")}
                                onAdd={() => openAddMeal(day.date, "SNACK")}
                                onRemove={(date, mealType) =>
                                    removeMeal(date, mealType)
                                }
                            />
                        </div>
                    ))}
                </div>
            </div>

            {/* Add Meal for Slot */}
            <AddMealModal 
                open={addMealModal.open}
                onClose={() =>
                    setAddMealModal(prev => ({
                        ...prev,
                        open: false
                    }))
                }
                type={addMealModal.type}
                date={addMealModal.day}
                onSelectRecipe={addMeal}
            />

            {/* Generate Plan Modal */}
            <GeneratePlanModal 
                open={generatePlanModal.open}
                onClose={() =>
                    setGeneratePlanModal({open: false})
                }
                onGenerated={(generatedMeals) => {
                    setGeneratedMeals(generatedMeals);
                }}
            />
        </>
    );
}

export default Home;
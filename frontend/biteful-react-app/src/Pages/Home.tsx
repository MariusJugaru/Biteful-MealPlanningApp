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

    const [meals, setMeals] = useState<MealResponse[]>([]);

    const [generatePlanModal, setGeneratePlanModal] = useState<{
        open: boolean;
    }>({
        open: false,
    });

    const [addMealModal, setAddMealModal] = useState<{
        open: boolean;
        day: string;
        type: "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK";
    }>({
        open: false,
        day: "",
        type: "BREAKFAST",
    });
    
    const token = localStorage.getItem("token");

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

    const getMeal = (
        date: string,
        type: string
    ) => {
        return meals.find(
            meal =>
                meal.date === date &&
                meal.type === type
        );
    };

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

    return (
        <>
            <Header />

            <div className="mx-auto px-12 py-8 select-none">

                <StatsSection
                    totalWeeklyCalories={
                        meals.reduce(
                            (sum, meal) =>
                                sum + meal.calories,
                            0
                        )
                    }
                />

                <div className="flex items-center justify-between flex-wrap gap-4">
                    <div className="flex items-center gap-4">
                        <h3 className="mt-4 mb-4">Weekly Meal Plan</h3>
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
                    <button
                        onClick={() => setGeneratePlanModal({ open: true })}
                        className="flex items-center gap-2 bg-black text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Generate Plan
                    </button>
                </div>

                <div className="hidden lg:grid lg:grid-cols-7 gap-4">
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
            <GeneratePlanModal 
                open={generatePlanModal.open}
                onClose={() =>
                    setGeneratePlanModal({open: false})
                }
                onGenerated={(generatedMeals) => {
                    setMeals(generatedMeals);
                }}
            />
        </>
    );
}

export default Home;
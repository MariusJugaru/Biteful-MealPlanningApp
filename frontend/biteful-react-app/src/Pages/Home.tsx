import { Link } from "react-router-dom";

import Header from "../Components/Header";
import StatsSection from "../Components/StatsSection";
import MealSlot from "../Components/MealSlot";
import { useEffect, useState } from "react";
import { config } from "../config";

const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

type MealResponse = {
    recipeId: string;
    title: string;
    type: "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK";
    date: string;
    image: string | null;
    calories: number;
};

function getWeekBounds() {
    const now = new Date();

    const monday = new Date(now);
    const day = monday.getDay();

    const diff = day === 0 ? -6 : 1 - day;

    monday.setDate(now.getDate() + diff);

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
    const [meals, setMeals] = useState<MealResponse[]>([]);

    const { startDate, endDate, dates } = getWeekBounds();

    useEffect(() => {
        const token = localStorage.getItem("token");
        
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

     return (
        <>
            <Header />

            <div className="mx-auto px-12 py-8">

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
                        <div className="rounded-md border px-2 py-1 text-xs font-semibold">{startDate} - {endDate}</div>
                    </div>
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
                                mealType="Breakfast"
                                recipe={getMeal(
                                    day.date,
                                    "BREAKFAST"
                                )}
                            />

                            <MealSlot
                                mealType="Lunch"
                                recipe={getMeal(
                                    day.date,
                                    "LUNCH"
                                )}
                            />

                            <MealSlot
                                mealType="Dinner"
                                recipe={getMeal(
                                    day.date,
                                    "DINNER"
                                )}
                            />

                            <MealSlot
                                mealType="Snack"
                                recipe={getMeal(
                                    day.date,
                                    "SNACK"
                                )}
                            />
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default Home;
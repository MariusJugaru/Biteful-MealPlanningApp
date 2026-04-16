import { Link } from "react-router-dom";

import Header from "../Components/Header";
import StatsSection from "../Components/StatsSection";


function Home() {
    console.log("Home rendered");
    return(
        <>
            <Header />
            <div className="mx-auto px-6 py-8">
                <StatsSection totalWeeklyCalories={6000}/>
                <h3>Weekly Meal Plan</h3>
            </div>
            <Link to="/my-recipes">Recipes</Link>
        </>
        
    );
}

export default Home;
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";

// Pages
import Home from "./Pages/Home";
import Recipes from "./Pages/Recipes";
import Saved from "./Pages/Saved";
import SavedRecipe from "./Pages/SavedRecipe";
import RecipeForm from "./Pages/RecipeForm";


function App() {
  localStorage.setItem("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzZWZkYWUwMC1lMjlhLTQwNDItODYzOS00MTE1MmMxNjM2YjAiLCJ1c2VybmFtZSI6InRlc3QiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3Njg1Njk5NiwiZXhwIjoxNzc2ODYwNTk2fQ.g4Y7vSk6PiYfQOlr8txCMCH-o2KUvMz-wM3fjdFlGPg");

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/recipes" element={<Recipes />} />
        <Route path="/saved" element={<Saved />} />
        <Route path="/saved/:id" element={<SavedRecipe />} />
        <Route path="/saved/:id/edit" element={<RecipeForm mode="edit"/>} />
        <Route path="/saved/add" element={<RecipeForm mode="create" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
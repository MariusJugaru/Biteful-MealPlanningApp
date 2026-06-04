import { BrowserRouter, Routes, Route } from "react-router-dom";

// Pages
import Home from "./Pages/Home";
import Recipes from "./Pages/Recipes";
import SavedRecipe from "./Pages/SavedRecipe";
import RecipeForm from "./Pages/RecipeForm";
import Register from "./Pages/Register";
import Login from "./Pages/Login";
import ProtectedRoute from "./ProtectedRoute";
import PublicRoute from "./PublicRoute";
import Logout from "./Pages/Logout";
import Lists from "./Pages/Lists";
import SavedLists from "./Pages/SavedLists"
import Users from "./Pages/Users";

import "react-date-range/dist/styles.css";
import "react-date-range/dist/theme/default.css";




function App() {
  // localStorage.setItem("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzZWZkYWUwMC1lMjlhLTQwNDItODYzOS00MTE1MmMxNjM2YjAiLCJ1c2VybmFtZSI6InRlc3QiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3Njg1Njk5NiwiZXhwIjoxNzc2ODYwNTk2fQ.g4Y7vSk6PiYfQOlr8txCMCH-o2KUvMz-wM3fjdFlGPg");

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ProtectedRoute><Home /></ProtectedRoute>} />
        <Route path="/recipes" element={<ProtectedRoute><Recipes mode="public" /></ProtectedRoute>} />
        <Route path="/recipes/:id" element={<ProtectedRoute><SavedRecipe mode="public" /></ProtectedRoute>} />
        <Route path="/saved" element={<ProtectedRoute><Recipes /></ProtectedRoute>} />
        <Route path="/saved/:id" element={<ProtectedRoute><SavedRecipe /></ProtectedRoute>} />
        <Route path="/saved/:id/edit" element={<ProtectedRoute><RecipeForm mode="edit"/></ProtectedRoute>} />
        <Route path="/saved/add" element={<ProtectedRoute><RecipeForm mode="create" /></ProtectedRoute>} />
        <Route path="/meals/:id" element={<ProtectedRoute><SavedRecipe mode="home" /></ProtectedRoute>} />
        <Route path="/lists" element={<ProtectedRoute><Lists /></ProtectedRoute>} />
        <Route path="/lists/:listId" element={<ProtectedRoute><SavedLists /></ProtectedRoute>} />
        <Route path="/users" element={<ProtectedRoute><Users /></ProtectedRoute>} />

        <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />
        <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
        <Route path="/logout" element={<Logout />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
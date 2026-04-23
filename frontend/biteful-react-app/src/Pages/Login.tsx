import { User, UtensilsCrossed, Mail, Lock } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom"
import TextInputShort from "../Components/TextInputShort";

type LoginForm = {
    usernameOrEmail: string;
    password: string;
}

function Login() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<LoginForm>({
        usernameOrEmail: "",
        password: "",
    })

    const [errors, setErrors] = useState<Partial<Record<keyof LoginForm, string>>>({});
    const [serverMessage, setServerMessage] = useState<string>("");

    const validate = () => {
        const errors: Partial<Record<keyof LoginForm, string>> = {};

        if (!formData.usernameOrEmail.trim()) {
            errors.usernameOrEmail = "Username or Email is required";
        }

        if (!formData.password.trim()) {
            errors.password = "Password is required"
        }

        return errors;
    }

    const handleSubmit = async () => {
        const validationErrors = validate();

        if (Object.keys(validationErrors).length > 0) {
            // Handle errors
            setErrors(validationErrors);
            console.log(validationErrors);
            return;
        }

        setErrors({});

        // send payload
        const response = await fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });


        const data = await response.json();

        if (!response.ok) {
            console.log(data.message);
            setServerMessage(data.message);
            return;
        }

        localStorage.setItem("token", data.token);

        navigate("/");
    }

    return (
        <div className="min-h-screen max-h-screen flex items-center justify-center px-4 py-8">
            <div className="w-full max-w-md flex flex-col gap-6 rounded-xl lg:border md:border md:p-8 ">
                {/* Header */}
                <div className="text-center mb-6">
                    <div className="inline-flex items-center justify-center w-16 h-16 bg-[#030213] text-[#ffffff] rounded-2xl mb-4">
                        <UtensilsCrossed className="w-8 h-8" />
                    </div>
                    <h1 className="text-3xl mb-2">Create Account</h1>
                    <p className="text-[#868686]">Start planning your meals today!</p>
                </div>

                {/* Form */}
                <div className="space-y-6 mb-8">
                    {/* Fields */}
                    <TextInputShort
                        image={User}
                        field="Username or Email"
                        val={formData.usernameOrEmail}
                        placeHolder="JohnDoe123"
                        onChange={(value) =>
                            setFormData({ ...formData, usernameOrEmail: value})
                        }
                        className="space-y-2"

                        error={errors.usernameOrEmail}
                    />

                    <TextInputShort
                        image={Lock}
                        field="Password"
                        val={formData.password}
                        placeHolder="Create a password"
                        onChange={(value) =>
                            setFormData({ ...formData, password: value})
                        }
                        showToggle={true}
                        className="space-y-2"

                        error={errors.password}
                    />

                </div>

                <button
                    onClick={handleSubmit}
                    className="bg-black text-sm text-white rounded-xl p-2"
                >
                    Sign in
                </button>

                <div className="mt-6 border-b" />

                <div className="flex items-center justify-center gap-2">
                    <div className="text-sm">Don't have an account?</div>
                    <button
                        onClick={() => {
                            navigate("/register")
                        }}
                        className="text-sm"
                    >
                        Sign up
                    </button>
                </div>

            </div>
        </div>
    );
}

export default Login
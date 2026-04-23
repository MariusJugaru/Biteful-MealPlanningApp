import { User, UtensilsCrossed, Mail, Lock } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom"
import TextInputShort from "../Components/TextInputShort";

type RegisterForm = {
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
}

function Register() {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState<RegisterForm>({
        username: "",
        email: "",
        password: "",
        confirmPassword: ""
    })

    const [errors, setErrors] = useState<Partial<Record<keyof RegisterForm, string>>>({});
    const [serverMessage, setServerMessage] = useState<string>("");

    const validate = () => {
        const errors: Partial<Record<keyof RegisterForm, string>> = {};

        if (!formData.username.trim()) {
            errors.username = "Username is required";
        }

        if (formData.username.trim().length < 3 || formData.username.trim().length > 20) {
            errors.username = "Username must have a length between 4 and 20 characters";
        }

        if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            errors.email = "Invalid email";
        }

        if (formData.password.length < 8) {
            errors.password = "Password must have at least 8 characters";
        } else if (!formData.password.match(/(?=.*[a-z])/)) {
            errors.password = "Password must contain an lower letter"
        } else if (!formData.password.match(/(?=.*[A-Z])/)) {
             errors.password = "Password must contain an upper letter"
        } else if (!formData.password.match(/(?=.*\d)/)) {
             errors.password = "Password must contain an number"
        } else if (!formData.password.match(/(?=.*[@$!%*?&])/)) {
             errors.password = "Password must contain a special character"
        }

        if (formData.password !== formData.confirmPassword) {
            errors.confirmPassword = "Passwords do not match";
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
        const { confirmPassword, ...payload } = formData;
        console.log(payload);

        const response = await fetch("http://localhost:8080/api/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
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
                        field="Username"
                        val={formData.username}
                        placeHolder="JohnDoe123"
                        onChange={(value) =>
                            setFormData({ ...formData, username: value})
                        }
                        className="space-y-2"

                        error={errors.username}
                    />

                    <TextInputShort
                        image={Mail}
                        field="Email"
                        val={formData.email}
                        placeHolder="example@email.com"
                        onChange={(value) =>
                            setFormData({ ...formData, email: value})
                        }
                        className="space-y-2"

                        error={errors.email}
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

                    <TextInputShort
                        image={Lock}
                        field="Confirm Password"
                        val={formData.confirmPassword}
                        placeHolder="Confirm the password"
                        onChange={(value) =>
                            setFormData({ ...formData, confirmPassword: value})
                        }
                        showToggle={true}
                        className="space-y-2"

                        error={errors.confirmPassword}
                    />
                </div>

                <button
                    onClick={handleSubmit}
                    className="bg-black text-sm text-white rounded-xl p-2"
                >
                    Create Account
                </button>

                <div className="mt-6 border-b" />

                <div className="flex items-center justify-center gap-2">
                    <div className="text-sm">Already have an account?</div>
                    <button
                        onClick={() => {
                            navigate("/login")
                        }}
                        className="text-sm"
                    >
                        Sign in
                    </button>
                </div>

            </div>
        </div>
    );
}

export default Register
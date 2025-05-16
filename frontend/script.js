document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("login-form");
  const registerForm = document.getElementById("register-form");
  const fetchCoursesButton = document.getElementById("fetch-courses");
  const coursesList = document.getElementById("courses-list");
  const dashboard = document.getElementById("dashboard");
  const authSection = document.getElementById("auth-section");

  const API_BASE_URL = "http://localhost:8080/api";

  // Handle login
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem("token", data.token);
        alert("Login successful!");
        authSection.style.display = "none";
        dashboard.style.display = "block";
      } else {
        alert("Login failed!");
      }
    } catch (error) {
      console.error("Error during login:", error);
    }
  });

  // Handle registration
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("register-email").value;
    const password = document.getElementById("register-password").value;

    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        alert("Registration successful!");
      } else {
        alert("Registration failed!");
      }
    } catch (error) {
      console.error("Error during registration:", error);
    }
  });

  // Fetch courses
  fetchCoursesButton.addEventListener("click", async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/courses`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.ok) {
        const courses = await response.json();
        coursesList.innerHTML = "";
        courses.forEach((course) => {
          const li = document.createElement("li");
          li.textContent = course.title;
          coursesList.appendChild(li);
        });
      } else {
        alert("Failed to fetch courses!");
      }
    } catch (error) {
      console.error("Error fetching courses:", error);
    }
  });
});

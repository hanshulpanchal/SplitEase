// ========== LOGIN HANDLER ==========
document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();

  try {
    const res = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (res.ok) {
      const { token } = await res.json();
      localStorage.setItem("token", token);
      window.location.href = "dashboard.html";
    } else {
      document.getElementById("loginError").textContent = "Invalid credentials";
    }
  } catch (err) {
    console.error("Login error:", err);
    document.getElementById("loginError").textContent = "Server error. Please try again later.";
  }
});

// ========== LOGOUT HANDLER ==========
document.getElementById("logoutBtn")?.addEventListener("click", () => {
  localStorage.removeItem("token");
  window.location.href = "index.html";
});

// ========== DASHBOARD PROTECTED API CALL ==========
async function fetchExpenses() {
  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "index.html";
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/api/expenses", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (res.status === 401) {
      alert("Unauthorized. Please login again.");
      window.location.href = "index.html";
      return;
    }

    const data = await res.json();
    console.log("Expenses:", data);

    const container = document.getElementById("expensesContainer");
    if (container) {
      container.innerHTML = data.map(expense => `
        <div class="expense-item">
          <h4>${expense.description}</h4>
          <p>Amount: ₹${expense.amount}</p>
          <p>Group: ${expense.group?.name || 'N/A'}</p>
        </div>
      `).join("");
    }
  } catch (err) {
    console.error("Error fetching expenses:", err);
  }
}

// Automatically fetch data if on dashboard page
if (window.location.pathname.includes("dashboard.html")) {
  fetchExpenses();
}

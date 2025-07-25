# BloodMate - Modern Blood Donation Management System (Web)

BloodMate is a next-generation blood donation management system, reimagined as a modern, full-stack web application. It connects donors and recipients, manages blood inventory and donation campaigns, and provides a creative, interactive dashboard experience.

---

## Features

### Donor Management
- Online donor registration & profile
- Eligibility checking and history
- Search, filter, and update donors
- Emergency donor availability

### Blood Matching System
- Blood type compatibility & smart matching
- Location-based donor search
- Emergency blood request handling

### Rewards & Gamification
- Points-based rewards (with tiers: Bronze, Silver, Gold, Platinum, Emergency Hero)
- Animated leaderboard
- Badges, confetti, and streak progress bars
- Top donors and statistics

### Blood Inventory
- Real-time stock tracking
- Expiry & low stock alerts
- Unit reservation and minimum stock management

### Campaigns
- Campaign creation, registration, and stats
- Location & blood group filtering
- Campaign history and success rates

### Advanced Dashboard Widgets
- **AI Chatbot** for blood donation Q&A
- **Gamified badges/confetti**
- **Global donor map**
- **Voice search & commands**
- **Animated leaderboard**
- **Easter eggs & surprises**
- **Donation streak progress bars**
- **Dark mode & accessibility**

---

## Technical Stack

- **Backend:** Java Spring Boot REST API (`localhost:8080`)
    - Controllers for donors, campaigns, rewards, inventory, stats, matching
    - Persistent storage (file or DB)
    - CORS enabled for frontend
- **Frontend:** Responsive web dashboard (`localhost:8082`)
    - Modern HTML, CSS, JS (modular)
    - Advanced interactive UI/UX
    - All data via backend API (no localStorage/mock data)
    - Sidebar widgets: collapsible, accessible, vertical under menu
- **Deployment:**
    - Backend: `mvn spring-boot:run` (Java 17+)
    - Frontend: `python server.py` (or any static server)

---

## Project Structure

```
BloodMate/
├── BloodMate/           # Java Spring Boot backend
│   └── src/main/java/com/blooddonation/webapi/
│       ├── controller/
│       ├── model/
│       ├── service/
│       └── ...
├── BloodMate-Web/       # Frontend web app
│   ├── index.html
│   ├── js/
│   ├── styles/
│   ├── server.py
│   └── ...
└── README.md
```

---

## Getting Started

### 1. Clone the repository

### 2. Start Backend API
```bash
cd BloodMate/BloodMate
mvn spring-boot:run
```
API: http://localhost:8080

### 3. Start Frontend
```bash
cd BloodMate/BloodMate-Web
python server.py
```
App: http://localhost:8082

---

## Usage

### Registering a Donor
1. Click "Register Donor" in the main menu
2. Fill in the required information
3. Submit the form

### Checking Eligibility
1. Click "Check Eligibility"
2. Enter donor information
3. View eligibility results

### Emergency Request
1. Click "Emergency Request"
2. Select blood group and units needed
3. Add any additional notes
4. Submit request

### Viewing Rewards
1. Click "Donor Rewards"
2. Enter donor ID
3. View points and earned rewards
4. Check top donors list

### Managing Campaigns
1. Create new campaigns
2. Register donors
3. Track campaign progress
4. View campaign statistics

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
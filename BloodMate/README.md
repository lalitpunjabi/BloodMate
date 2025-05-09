# BloodMate - Blood Donation Management System

BloodMate is a comprehensive blood donation management system that helps connect donors with recipients, manage blood inventory, and track donation campaigns.

## Features

### 1. Donor Management
- Donor registration with detailed information
- Donor eligibility checking
- Donor search and filtering
- Donor profile updates
- Emergency donor availability tracking

### 2. Blood Matching System
- Blood type compatibility checking
- Donor-recipient matching
- Location-based donor search
- Emergency blood request handling

### 3. Rewards System
- Points-based reward program
- Different point values for blood types:
  - O-: 100 points (Universal donor)
  - O+: 80 points
  - A-: 80 points
  - A+: 70 points
  - B-: 80 points
  - B+: 70 points
  - AB-: 70 points
  - AB+: 60 points
- Reward tiers:
  - Bronze Donor (200 points)
  - Silver Donor (500 points)
  - Gold Donor (1000 points)
  - Platinum Donor (2000 points)
  - Emergency Hero (300 points)
- Top donors leaderboard
- Donor statistics tracking

### 4. Blood Inventory Management
- Real-time blood stock tracking
- Expiration date monitoring
- Low stock alerts
- Blood unit reservation system
- Minimum stock level management
- Expiring blood unit notifications

### 5. Campaign Management
- Blood donation campaign creation
- Campaign registration
- Campaign statistics
- Location-based campaign filtering
- Blood group-specific campaigns
- Campaign history tracking

### 6. Statistics and Reporting
- General donation statistics
- Blood group distribution
- Campaign success rates
- Donor participation metrics
- Emergency response statistics

## Technical Details

### Prerequisites
- Java 17 or higher
- JavaFX
- Maven

### Project Structure
```
src/main/java/com/blooddonation/
├── BloodDonationGUI.java      # Main GUI application
├── BloodInventory.java        # Blood inventory management
├── BloodMatcher.java          # Blood matching logic
├── CampaignManager.java       # Campaign management
├── Donor.java                 # Donor entity
├── DonorManager.java          # Donor management
├── EligibilityChecker.java    # Donor eligibility checking
├── RewardsManager.java        # Rewards system
├── RewardsPanel.java          # Rewards GUI
└── StatisticsManager.java     # Statistics and reporting
```

### Key Classes

#### BloodDonationGUI
- Main application window
- Menu system
- Form handling
- Alert system
- Navigation controls

#### Donor
- Unique ID generation
- Personal information
- Medical history
- Emergency contact details
- Donation preferences

#### BloodInventory
- Blood unit tracking
- Expiration management
- Stock level monitoring
- Reservation system

#### CampaignManager
- Campaign creation and management
- Donor registration
- Campaign statistics
- Location-based filtering

#### RewardsManager
- Points system
- Reward tracking
- Donor statistics
- Leaderboard management

### Data Storage
- Donor information stored in `donors.txt`
- Persistent storage for donor records
- Campaign history tracking
- Reward history maintenance

## Getting Started

1. Clone the repository
2. Ensure Java 17+ and Maven are installed
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn javafx:run
   ```

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
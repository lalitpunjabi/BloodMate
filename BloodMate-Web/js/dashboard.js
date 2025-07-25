// Dashboard JavaScript for BloodMate
class BloodMateApp {
    constructor() {
        this.donors = JSON.parse(localStorage.getItem('bloodmate_donors') || '[]');
        this.campaigns = JSON.parse(localStorage.getItem('bloodmate_campaigns') || '[]');
        this.inventory = JSON.parse(localStorage.getItem('bloodmate_inventory') || '{}');
        this.rewards = JSON.parse(localStorage.getItem('bloodmate_rewards') || '{}');
        this.currentSection = 'overview';
        
        this.initializeDashboard();
        this.loadSampleData();
    }
    
    initializeDashboard() {
        this.setupMenuHandlers();
        this.loadDashboardSection('overview');
    }
    
    setupMenuHandlers() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                const section = e.target.closest('.menu-item').dataset.section;
                this.loadDashboardSection(section);
                
                // Update active state
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.target.closest('.menu-item').classList.add('active');
            });
        });
    }
    
    loadDashboardSection(section) {
        this.currentSection = section;
        const content = document.getElementById('dashboard-content');
        
        switch(section) {
            case 'overview':
                content.innerHTML = this.renderOverview();
                break;
            case 'register-donor':
                content.innerHTML = this.renderRegisterDonor();
                this.setupRegisterDonorForm();
                break;
            case 'check-eligibility':
                content.innerHTML = this.renderCheckEligibility();
                this.setupEligibilityForm();
                break;
            case 'find-matches':
                content.innerHTML = this.renderFindMatches();
                this.setupFindMatchesForm();
                break;
            case 'view-donors':
                content.innerHTML = this.renderViewDonors();
                this.setupDonorFilters();
                break;
            case 'emergency-request':
                content.innerHTML = this.renderEmergencyRequest();
                this.setupEmergencyForm();
                break;
            case 'campaigns':
                content.innerHTML = this.renderCampaigns();
                this.setupCampaignHandlers();
                break;
            case 'rewards':
                content.innerHTML = this.renderRewards();
                this.setupRewardsHandlers();
                break;
            case 'statistics':
                content.innerHTML = this.renderStatistics();
                this.setupStatisticsCharts();
                break;
            default:
                content.innerHTML = this.renderOverview();
        }
    }
    
    renderOverview() {
        const totalDonors = this.donors.length;
        const bloodGroups = this.getBloodGroupDistribution();
        const recentDonations = this.getRecentDonations();
        
        return `
            <div class="overview-section">
                <h2 class="section-title">Dashboard Overview</h2>
                
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-info">
                            <h3>${totalDonors}</h3>
                            <p>Total Donors</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-tint"></i>
                        </div>
                        <div class="stat-info">
                            <h3>${this.getTotalDonations()}</h3>
                            <p>Total Donations</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-calendar"></i>
                        </div>
                        <div class="stat-info">
                            <h3>${this.campaigns.length}</h3>
                            <p>Active Campaigns</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-exclamation-triangle"></i>
                        </div>
                        <div class="stat-info">
                            <h3>${this.getEmergencyRequests()}</h3>
                            <p>Emergency Requests</p>
                        </div>
                    </div>
                </div>
                
                <div class="overview-grid">
                    <div class="overview-card">
                        <h3>Blood Group Distribution</h3>
                        <div class="blood-group-chart">
                            ${Object.entries(bloodGroups).map(([group, count]) => `
                                <div class="blood-group-item">
                                    <span class="group-name">${group}</span>
                                    <div class="group-bar">
                                        <div class="group-fill" style="width: ${(count / totalDonors) * 100}%"></div>
                                    </div>
                                    <span class="group-count">${count}</span>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                    
                    <div class="overview-card">
                        <h3>Recent Activity</h3>
                        <div class="activity-list">
                            ${recentDonations.map(donation => `
                                <div class="activity-item">
                                    <div class="activity-icon">
                                        <i class="fas fa-tint"></i>
                                    </div>
                                    <div class="activity-info">
                                        <p><strong>${donation.donorName}</strong> donated ${donation.bloodGroup}</p>
                                        <small>${this.formatDate(donation.date)}</small>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </div>
                
                <div class="quick-actions">
                    <h3>Quick Actions</h3>
                    <div class="action-buttons">
                        <button class="btn btn-primary" onclick="app.loadDashboardSection('register-donor')">
                            <i class="fas fa-user-plus"></i>
                            Register New Donor
                        </button>
                        <button class="btn btn-danger" onclick="app.loadDashboardSection('emergency-request')">
                            <i class="fas fa-exclamation-triangle"></i>
                            Emergency Request
                        </button>
                        <button class="btn btn-secondary" onclick="app.loadDashboardSection('campaigns')">
                            <i class="fas fa-calendar-plus"></i>
                            Create Campaign
                        </button>
                    </div>
                </div>
            </div>
        `;
    }
    
    renderRegisterDonor() {
        return `
            <div class="form-section">
                <h2 class="section-title">Register New Donor</h2>
                
                <form id="donor-registration-form" class="dashboard-form">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="donor-name">Full Name *</label>
                            <input type="text" id="donor-name" name="name" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-email">Email *</label>
                            <input type="email" id="donor-email" name="email" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-phone">Phone Number *</label>
                            <input type="tel" id="donor-phone" name="phone" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-age">Age *</label>
                            <input type="number" id="donor-age" name="age" min="18" max="65" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-blood-group">Blood Group *</label>
                            <select id="donor-blood-group" name="bloodGroup" required>
                                <option value="">Select Blood Group</option>
                                <option value="A+">A+</option>
                                <option value="A-">A-</option>
                                <option value="B+">B+</option>
                                <option value="B-">B-</option>
                                <option value="AB+">AB+</option>
                                <option value="AB-">AB-</option>
                                <option value="O+">O+</option>
                                <option value="O-">O-</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-weight">Weight (kg) *</label>
                            <input type="number" id="donor-weight" name="weight" min="50" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-address">Address *</label>
                            <textarea id="donor-address" name="address" rows="3" required></textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="donor-city">City *</label>
                            <input type="text" id="donor-city" name="city" required>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-large">
                            <i class="fas fa-user-plus"></i>
                            Register Donor
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-undo"></i>
                            Reset Form
                        </button>
                    </div>
                </form>
            </div>
        `;
    }
    
    // Helper methods
    getBloodGroupDistribution() {
        const distribution = {};
        this.donors.forEach(donor => {
            distribution[donor.bloodGroup] = (distribution[donor.bloodGroup] || 0) + 1;
        });
        return distribution;
    }
    
    getTotalDonations() {
        return this.donors.reduce((total, donor) => total + (donor.donationCount || 1), 0);
    }
    
    getEmergencyRequests() {
        return Math.floor(Math.random() * 5) + 1; // Simulated data
    }
    
    getRecentDonations() {
        return this.donors.slice(0, 5).map(donor => ({
            donorName: donor.name,
            bloodGroup: donor.bloodGroup,
            date: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000)
        }));
    }
    
    formatDate(date) {
        return new Date(date).toLocaleDateString();
    }
    
    setupRegisterDonorForm() {
        const form = document.getElementById('donor-registration-form');
        if (form) {
            form.addEventListener('submit', (e) => {
                e.preventDefault();
                this.registerDonor(new FormData(form));
            });
        }
    }
    
    registerDonor(formData) {
        const donor = {
            id: 'D' + Date.now(),
            name: formData.get('name'),
            email: formData.get('email'),
            phone: formData.get('phone'),
            age: parseInt(formData.get('age')),
            bloodGroup: formData.get('bloodGroup'),
            weight: parseInt(formData.get('weight')),
            address: formData.get('address'),
            city: formData.get('city'),
            registrationDate: new Date(),
            donationCount: 0,
            lastDonation: null
        };
        
        this.donors.push(donor);
        this.saveDonors();
        
        this.showNotification('Donor registered successfully!', 'success');
        document.getElementById('donor-registration-form').reset();
    }
    
    saveDonors() {
        localStorage.setItem('bloodmate_donors', JSON.stringify(this.donors));
    }
    
    loadSampleData() {
        if (this.donors.length === 0) {
            this.donors = [
                { id: 'D001', name: 'John Doe', bloodGroup: 'O+', age: 28, city: 'New York', phone: '123-456-7890', donationCount: 5 },
                { id: 'D002', name: 'Jane Smith', bloodGroup: 'A-', age: 32, city: 'Los Angeles', phone: '098-765-4321', donationCount: 3 },
                { id: 'D003', name: 'Mike Johnson', bloodGroup: 'B+', age: 25, city: 'Chicago', phone: '555-123-4567', donationCount: 7 }
            ];
            this.saveDonors();
        }
        
        if (this.campaigns.length === 0) {
            this.campaigns = [
                { id: 'C001', name: 'Save Lives Campaign', date: new Date(), location: 'Community Center', target: 100, registrations: 75, status: 'active' },
                { id: 'C002', name: 'Emergency Blood Drive', date: new Date(), location: 'Hospital', target: 50, registrations: 30, status: 'active' }
            ];
            localStorage.setItem('bloodmate_campaigns', JSON.stringify(this.campaigns));
        }
    }
    
    showNotification(message, type) {
        // Reuse the notification function from main.js
        if (typeof showNotification === 'function') {
            showNotification(message, type);
        } else {
            alert(message);
        }
    }
}

// Initialize the app when dashboard is loaded
function loadDashboardSection(section) {
    if (typeof app !== 'undefined') {
        app.loadDashboardSection(section);
    }
}

// Initialize app when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('dashboard')) {
        window.app = new BloodMateApp();
    }
});

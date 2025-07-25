// leaderboard.js
// Animated leaderboard for top donors/campaigns
class AnimatedLeaderboard {
    constructor() {
        this.renderLeaderboard();
    }
    renderLeaderboard() {
        const lb = document.createElement('div');
        lb.id = 'animated-leaderboard';
        lb.innerHTML = `<h3>Top Donors</h3><ol class="leaderboard-list"></ol>`;
        const sidebarWidgets = document.querySelector('.dashboard-sidebar .dashboard-widgets');
if (sidebarWidgets) sidebarWidgets.appendChild(lb);
        this.list = lb.querySelector('.leaderboard-list');
        this.loadLeaderboard();
    }
    loadLeaderboard() {
        // Simulate leaderboard data
        const data = [
            { name: 'Amit', points: 120, avatar: '🦸' },
            { name: 'Priya', points: 110, avatar: '🦸‍♀️' },
            { name: 'Rahul', points: 100, avatar: '🧑‍⚕️' },
            { name: 'Sara', points: 95, avatar: '🧑‍🎤' },
            { name: 'Lalit', points: 90, avatar: '🦸' }
        ];
        this.list.innerHTML = '';
        data.forEach((user, i) => {
            const li = document.createElement('li');
            li.className = 'leaderboard-entry';
            li.innerHTML = `<span class="avatar">${user.avatar}</span> <span class="name">${user.name}</span> <span class="points">${user.points} pts</span>`;
            li.style.animationDelay = (i * 0.2) + 's';
            this.list.appendChild(li);
        });
    }
}
window.AnimatedLeaderboard = AnimatedLeaderboard;
window.addEventListener('DOMContentLoaded', () => {
    window.animatedLeaderboard = new AnimatedLeaderboard();
});

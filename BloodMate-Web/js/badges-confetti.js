// badges-confetti.js
// Gamified donor badges and confetti/fireworks system
class BadgeConfetti {
    constructor() {
        this.badges = [
            { name: 'First Donation', icon: '🥇', desc: 'Congratulations on your first donation!' },
            { name: '5 Donations', icon: '🏅', desc: '5 donations! You are a lifesaver!' },
            { name: 'Rare Blood Hero', icon: '🦸', desc: 'Rare blood type donor!' },
            { name: 'Emergency Hero', icon: '🚨', desc: 'Donated during emergency!' },
            { name: 'Streak Master', icon: '🔥', desc: 'Multiple donations in a row!' }
        ];
        this.renderBadgePanel();
    }

    renderBadgePanel() {
        // Add badge panel to dashboard
        const panel = document.createElement('div');
        panel.id = 'badge-confetti-panel';
        panel.innerHTML = `<h3>Badges</h3><div class="badge-list"></div>`;
        document.body.appendChild(panel);
        this.badgeList = panel.querySelector('.badge-list');
        this.loadBadges();
    }

    loadBadges() {
        // Simulate unlocked badges
        let unlocked = JSON.parse(localStorage.getItem('bloodmate_badges') || '[]');
        this.badgeList.innerHTML = '';
        this.badges.forEach((badge, i) => {
            const unlockedBadge = unlocked.includes(badge.name);
            const badgeEl = document.createElement('div');
            badgeEl.className = 'badge' + (unlockedBadge ? ' unlocked' : '');
            badgeEl.innerHTML = `<span class="badge-icon">${badge.icon}</span><span class="badge-name">${badge.name}</span>`;
            badgeEl.title = badge.desc;
            this.badgeList.appendChild(badgeEl);
        });
    }

    unlockBadge(badgeName) {
        let unlocked = JSON.parse(localStorage.getItem('bloodmate_badges') || '[]');
        if (!unlocked.includes(badgeName)) {
            unlocked.push(badgeName);
            localStorage.setItem('bloodmate_badges', JSON.stringify(unlocked));
            this.loadBadges();
            this.showConfetti();
        }
    }

    showConfetti() {
        // Simple confetti animation
        for (let i = 0; i < 80; i++) {
            const conf = document.createElement('div');
            conf.className = 'confetti';
            conf.style.left = Math.random() * 100 + 'vw';
            conf.style.animationDuration = (1 + Math.random() * 2) + 's';
            conf.style.background = `hsl(${Math.random()*360}, 80%, 60%)`;
            document.body.appendChild(conf);
            setTimeout(() => conf.remove(), 2500);
        }
    }
}

window.BadgeConfetti = BadgeConfetti;
window.addEventListener('DOMContentLoaded', () => {
    window.badgeConfetti = new BadgeConfetti();
});

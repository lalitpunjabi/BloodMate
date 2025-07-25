// streak-progress.js
// Animated donation streak progress bars
class StreakProgress {
    constructor() {
        this.renderStreakBar();
    }
    renderStreakBar() {
        const bar = document.createElement('div');
        bar.id = 'streak-progress-bar';
        bar.innerHTML = `
            <h3>Donation Streak</h3>
            <div class="streak-bar-bg">
                <div class="streak-bar-fill"></div>
                <span class="streak-label">0 days</span>
            </div>
        `;
        document.body.appendChild(bar);
        this.fill = bar.querySelector('.streak-bar-fill');
        this.label = bar.querySelector('.streak-label');
        this.updateStreak();
    }
    updateStreak() {
        // Simulate streak (randomized for now)
        const streak = Math.floor(Math.random()*120);
        this.fill.style.width = Math.min(streak,100) + '%';
        this.fill.classList.add('glow');
        this.label.textContent = streak + ' days';
    }
}
window.StreakProgress = StreakProgress;
window.addEventListener('DOMContentLoaded', () => {
    window.streakProgress = new StreakProgress();
});

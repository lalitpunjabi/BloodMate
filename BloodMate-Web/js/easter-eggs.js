// easter-eggs.js
// Fun surprises and hidden features
class EasterEggs {
    constructor() {
        this.konamiCode = [38,38,40,40,37,39,37,39,66,65];
        this.konamiIndex = 0;
        this.setupKonami();
        this.setupHiddenBloodFountain();
    }
    setupKonami() {
        window.addEventListener('keydown', (e) => {
            if (e.keyCode === this.konamiCode[this.konamiIndex]) {
                this.konamiIndex++;
                if (this.konamiIndex === this.konamiCode.length) {
                    this.triggerBloodFountain();
                    this.konamiIndex = 0;
                }
            } else {
                this.konamiIndex = 0;
            }
        });
    }
    setupHiddenBloodFountain() {
        // Click on logo triggers blood fountain
        document.addEventListener('DOMContentLoaded', () => {
            const logo = document.querySelector('.site-logo');
            if (logo) {
                logo.addEventListener('click', () => this.triggerBloodFountain());
            }
        });
    }
    triggerBloodFountain() {
        for (let i = 0; i < 50; i++) {
            const drop = document.createElement('div');
            drop.className = 'blood-fountain';
            drop.style.left = (50 + Math.random()*30 - 15) + 'vw';
            drop.style.animationDelay = (Math.random()*0.4) + 's';
            document.body.appendChild(drop);
            setTimeout(() => drop.remove(), 1200);
        }
    }
}
window.EasterEggs = EasterEggs;
window.addEventListener('DOMContentLoaded', () => {
    window.easterEggs = new EasterEggs();
});

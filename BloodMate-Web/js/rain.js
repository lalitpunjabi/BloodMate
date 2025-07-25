// Blood Drop Rain Effect
function createBloodDropRain() {
    const rainContainer = document.createElement('div');
    rainContainer.className = 'blood-rain crazy-rain';
    document.body.appendChild(rainContainer);
    for (let i = 0; i < 40; i++) {
        const drop = document.createElement('div');
        drop.className = 'rain-drop crazy-drop';
        drop.style.left = Math.random() * 100 + 'vw';
        drop.style.animationDuration = (Math.random() * 2 + 2) + 's';
        drop.style.animationDelay = (Math.random() * 3) + 's';
        drop.onclick = () => {
            drop.style.animationPlayState = 'paused';
            drop.style.background = '#fff';
            setTimeout(() => drop.style.animationPlayState = 'running', 600);
        };
        rainContainer.appendChild(drop);
    }
}

document.addEventListener('DOMContentLoaded', createBloodDropRain);

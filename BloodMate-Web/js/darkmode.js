// Dark Mode Toggle
function createDarkModeToggle() {
    const toggle = document.createElement('button');
    toggle.className = 'dark-mode-toggle btn btn-secondary';
    toggle.innerHTML = '<i class="fas fa-moon"></i> <span>Dark Mode</span>';
    toggle.style.position = 'fixed';
    toggle.style.bottom = '32px';
    toggle.style.right = '32px';
    toggle.style.zIndex = '9999';
    toggle.style.boxShadow = '0 2px 16px rgba(0,0,0,0.12)';
    toggle.onclick = () => {
        document.body.classList.toggle('dark-mode');
        localStorage.setItem('bloodmate_darkmode', document.body.classList.contains('dark-mode'));
        toggle.querySelector('i').className = document.body.classList.contains('dark-mode') ? 'fas fa-sun' : 'fas fa-moon';
        toggle.querySelector('span').textContent = document.body.classList.contains('dark-mode') ? 'Light Mode' : 'Dark Mode';
    };
    document.body.appendChild(toggle);
}

document.addEventListener('DOMContentLoaded', () => {
    if (localStorage.getItem('bloodmate_darkmode') === 'true') {
        document.body.classList.add('dark-mode');
    }
    createDarkModeToggle();
});

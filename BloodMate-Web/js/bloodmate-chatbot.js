// bloodmate-chatbot.js
// Floating AI Chatbot for BloodMate
class BloodMateChatbot {
    constructor() {
        this.init();
    }

    init() {
        // Create chatbot container
        this.container = document.createElement('div');
        this.container.id = 'bloodmate-chatbot';
        this.container.innerHTML = `
            <div class="chatbot-avatar" tabindex="0" aria-label="Open BloodMate Chatbot">
                <span class="chatbot-face">🤖</span>
                <span class="chatbot-bubble">Need help?</span>
            </div>
            <div class="chatbot-window" aria-live="polite">
                <div class="chatbot-header">BloodMate Assistant <button class="chatbot-close" title="Close">×</button></div>
                <div class="chatbot-messages"></div>
                <form class="chatbot-input-form"><input type="text" placeholder="Ask me anything..." aria-label="Chat input"/><button type="submit">Send</button></form>
            </div>
        `;
        document.body.appendChild(this.container);
        this.avatar = this.container.querySelector('.chatbot-avatar');
        this.window = this.container.querySelector('.chatbot-window');
        this.messages = this.container.querySelector('.chatbot-messages');
        this.inputForm = this.container.querySelector('.chatbot-input-form');
        this.input = this.inputForm.querySelector('input');
        this.closeBtn = this.container.querySelector('.chatbot-close');
        this.window.style.display = 'none';
        this.addListeners();
    }

    addListeners() {
        this.avatar.addEventListener('click', () => this.toggleWindow());
        this.avatar.addEventListener('keypress', (e) => { if (e.key === 'Enter' || e.key === ' ') this.toggleWindow(); });
        this.closeBtn.addEventListener('click', () => this.toggleWindow(false));
        this.inputForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const msg = this.input.value.trim();
            if (msg) {
                this.addMessage('user', msg);
                this.respond(msg);
                this.input.value = '';
            }
        });
    }

    toggleWindow(force) {
        if (force === false) {
            this.window.style.display = 'none';
            return;
        }
        this.window.style.display = this.window.style.display === 'none' ? 'block' : 'none';
        if (this.window.style.display === 'block') this.input.focus();
    }

    addMessage(sender, text) {
        const msg = document.createElement('div');
        msg.className = `chatbot-msg chatbot-msg-${sender}`;
        msg.innerText = text;
        this.messages.appendChild(msg);
        this.messages.scrollTop = this.messages.scrollHeight;
    }

    respond(userMsg) {
        // Simple canned responses for now
        let response = "I'm here to help!";
        if (/donat|blood/i.test(userMsg)) response = "You can donate blood every 3 months if you're healthy! Want to check your eligibility?";
        else if (/match|find/i.test(userMsg)) response = "Looking for a blood match? Go to the Find Matches section or tell me your blood group!";
        else if (/reward|badge/i.test(userMsg)) response = "Earn badges and rewards by donating more! Check the Rewards section.";
        else if (/campaign/i.test(userMsg)) response = "Campaigns help save more lives! Want to see ongoing campaigns?";
        else if (/emergency/i.test(userMsg)) response = "For emergencies, use the Emergency Request option. Stay calm and act fast!";
        else if (/hello|hi|hey/i.test(userMsg)) response = "Hello! How can I assist you today?";
        else if (/fact|tip/i.test(userMsg)) response = "Blood cannot be manufactured. Donating saves lives!";
        else if (/thank/i.test(userMsg)) response = "You're welcome! Every donor is a hero.";
        setTimeout(() => this.addMessage('bot', response), 600);
    }
}

window.addEventListener('DOMContentLoaded', () => {
    new BloodMateChatbot();
});

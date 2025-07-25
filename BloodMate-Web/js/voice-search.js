// voice-search.js
// Voice search and commands for BloodMate
class VoiceSearch {
    constructor() {
        this.supported = 'webkitSpeechRecognition' in window || 'SpeechRecognition' in window;
        if (!this.supported) return;
        this.recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
        this.recognition.lang = 'en-US';
        this.recognition.continuous = false;
        this.recognition.interimResults = false;
        this.createMicButton();
    }
    createMicButton() {
        this.micBtn = document.createElement('button');
        this.micBtn.id = 'voice-search-btn';
        this.micBtn.title = 'Voice Search';
        this.micBtn.innerHTML = '<span class="mic-icon">🎤</span>';
        document.body.appendChild(this.micBtn);
        this.micBtn.addEventListener('click', () => this.startRecognition());
    }
    startRecognition() {
        this.micBtn.classList.add('listening');
        this.recognition.start();
        this.recognition.onresult = (e) => {
            const transcript = e.results[0][0].transcript;
            this.handleVoiceCommand(transcript);
            this.micBtn.classList.remove('listening');
        };
        this.recognition.onerror = () => {
            this.micBtn.classList.remove('listening');
        };
    }
    handleVoiceCommand(cmd) {
        // Simple voice command handling
        if (/register|donor/i.test(cmd)) {
            alert('Taking you to Donor Registration!');
            // Could trigger navigation in SPA
        } else if (/find|match/i.test(cmd)) {
            alert('Opening Blood Match Finder!');
        } else if (/campaign/i.test(cmd)) {
            alert('Showing Campaigns!');
        } else if (/reward|badge/i.test(cmd)) {
            alert('Opening Rewards!');
        } else {
            alert('Voice command: ' + cmd);
        }
    }
}
window.VoiceSearch = VoiceSearch;
window.addEventListener('DOMContentLoaded', () => {
    window.voiceSearch = new VoiceSearch();
});

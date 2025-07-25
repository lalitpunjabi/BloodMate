// donor-map.js
// Interactive real-time global donor map
class DonorMap {
    constructor() {
        this.initMap();
    }
    initMap() {
        const mapContainer = document.createElement('div');
        mapContainer.id = 'donor-map';
        mapContainer.innerHTML = `<h3>Global Donor Map</h3><div id="map-frame"></div>`;
        document.body.appendChild(mapContainer);
        // For MVP, embed a styled OpenStreetMap iframe
        const iframe = document.createElement('iframe');
        iframe.src = 'https://www.openstreetmap.org/export/embed.html?bbox=68.0%2C6.0%2C97.0%2C37.0&layer=mapnik';
        iframe.width = '100%';
        iframe.height = '400';
        iframe.style.border = '1px solid #b71c1c';
        iframe.title = 'Global Donor Map';
        mapContainer.querySelector('#map-frame').appendChild(iframe);
        // Animated pins can be added later with a real API
    }
}
window.DonorMap = DonorMap;
window.addEventListener('DOMContentLoaded', () => {
    window.donorMap = new DonorMap();
});

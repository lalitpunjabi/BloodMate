// Animation utilities for BloodMate
class AnimationManager {
    constructor() {
        this.observers = new Map();
        this.init();
    }
    
    init() {
        this.setupIntersectionObservers();
        this.setupScrollAnimations();
        this.setupHoverEffects();
        this.setupLoadingAnimations();
    }
    
    setupIntersectionObservers() {
        // Fade in animation observer
        const fadeInObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-fade-in');
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        });
        
        // Slide in animation observer
        const slideInObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const direction = entry.target.dataset.slideDirection || 'up';
                    entry.target.classList.add(`animate-slide-in-${direction}`);
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translate(0, 0)';
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        });
        
        // Scale in animation observer
        const scaleInObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-scale-in');
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'scale(1)';
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        });
        
        this.observers.set('fadeIn', fadeInObserver);
        this.observers.set('slideIn', slideInObserver);
        this.observers.set('scaleIn', scaleInObserver);
        
        this.observeElements();
    }
    
    observeElements() {
        // Observe feature cards
        document.querySelectorAll('.feature-card').forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(30px)';
            card.style.transitionDelay = `${index * 0.1}s`;
            this.observers.get('fadeIn').observe(card);
        });
        
        // Observe stat cards
        document.querySelectorAll('.stat-card').forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'scale(0.8)';
            card.style.transitionDelay = `${index * 0.1}s`;
            this.observers.get('scaleIn').observe(card);
        });
        
        // Observe section titles
        document.querySelectorAll('.section-title').forEach(title => {
            title.style.opacity = '0';
            title.style.transform = 'translateY(20px)';
            this.observers.get('fadeIn').observe(title);
        });
        
        // Observe about features
        document.querySelectorAll('.about-feature').forEach((feature, index) => {
            feature.style.opacity = '0';
            feature.style.transform = 'translateX(-30px)';
            feature.style.transitionDelay = `${index * 0.2}s`;
            feature.dataset.slideDirection = 'right';
            this.observers.get('slideIn').observe(feature);
        });
    }
    
    setupScrollAnimations() {
        let ticking = false;
        
        const updateAnimations = () => {
            const scrollY = window.scrollY;
            const windowHeight = window.innerHeight;
            
            // Parallax effect for hero background shapes
            document.querySelectorAll('.bg-shape').forEach((shape, index) => {
                const speed = 0.5 + (index * 0.1);
                shape.style.transform = `translateY(${scrollY * speed}px)`;
            });
            
            // Navbar background opacity
            const navbar = document.getElementById('navbar');
            if (navbar) {
                const opacity = Math.min(scrollY / 100, 0.95);
                navbar.style.backgroundColor = `rgba(255, 255, 255, ${opacity})`;
            }
            
            ticking = false;
        };
        
        const requestTick = () => {
            if (!ticking) {
                requestAnimationFrame(updateAnimations);
                ticking = true;
            }
        };
        
        window.addEventListener('scroll', requestTick);
    }
    
    setupHoverEffects() {
        // Button ripple effect
        document.querySelectorAll('.btn').forEach(btn => {
            btn.addEventListener('click', this.createRipple.bind(this));
        });
        
        // Card hover effects
        document.querySelectorAll('.feature-card, .stat-card').forEach(card => {
            card.addEventListener('mouseenter', () => {
                card.style.transform = 'translateY(-8px) scale(1.02)';
            });
            
            card.addEventListener('mouseleave', () => {
                card.style.transform = 'translateY(0) scale(1)';
            });
        });
        
        // Blood drop hover animation
        document.querySelectorAll('.blood-drop').forEach(drop => {
            drop.addEventListener('mouseenter', () => {
                drop.classList.add('animate-heartbeat');
            });
            
            drop.addEventListener('mouseleave', () => {
                drop.classList.remove('animate-heartbeat');
            });
        });
    }
    
    createRipple(event) {
        const button = event.currentTarget;
        const circle = document.createElement('span');
        const diameter = Math.max(button.clientWidth, button.clientHeight);
        const radius = diameter / 2;
        
        const rect = button.getBoundingClientRect();
        circle.style.width = circle.style.height = `${diameter}px`;
        circle.style.left = `${event.clientX - rect.left - radius}px`;
        circle.style.top = `${event.clientY - rect.top - radius}px`;
        circle.classList.add('ripple');
        
        const ripple = button.getElementsByClassName('ripple')[0];
        if (ripple) {
            ripple.remove();
        }
        
        button.appendChild(circle);
        
        setTimeout(() => {
            circle.remove();
        }, 600);
    }
    
    setupLoadingAnimations() {
        // Loading spinner for async operations
        this.createLoadingSpinner();
        
        // Progress bar animations
        this.animateProgressBars();
    }
    
    createLoadingSpinner() {
        const style = document.createElement('style');
        style.textContent = `
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(255, 255, 255, 0.9);
                display: flex;
                justify-content: center;
                align-items: center;
                z-index: 10000;
                opacity: 0;
                visibility: hidden;
                transition: all 0.3s ease;
            }
            
            .loading-overlay.active {
                opacity: 1;
                visibility: visible;
            }
            
            .spinner {
                width: 50px;
                height: 50px;
                border: 4px solid var(--gray-200);
                border-top: 4px solid var(--primary-color);
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }
            
            .ripple {
                position: absolute;
                border-radius: 50%;
                background: rgba(255, 255, 255, 0.6);
                transform: scale(0);
                animation: ripple-animation 0.6s linear;
                pointer-events: none;
            }
            
            @keyframes ripple-animation {
                to {
                    transform: scale(4);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);
    }
    
    animateProgressBars() {
        const progressBars = document.querySelectorAll('.progress-fill');
        
        const progressObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const bar = entry.target;
                    const width = bar.style.width;
                    bar.style.width = '0%';
                    
                    setTimeout(() => {
                        bar.style.transition = 'width 1.5s ease-out';
                        bar.style.width = width;
                    }, 100);
                    
                    progressObserver.unobserve(bar);
                }
            });
        }, { threshold: 0.5 });
        
        progressBars.forEach(bar => {
            progressObserver.observe(bar);
        });
    }
    
    // Counter animation
    animateCounter(element, target, duration = 2000) {
        const start = parseInt(element.textContent) || 0;
        const increment = (target - start) / (duration / 16);
        let current = start;
        
        const updateCounter = () => {
            if (current < target) {
                current += increment;
                element.textContent = Math.floor(current);
                requestAnimationFrame(updateCounter);
            } else {
                element.textContent = target;
            }
        };
        
        updateCounter();
    }
    
    // Typewriter effect
    typeWriter(element, text, speed = 50) {
        element.textContent = '';
        let i = 0;
        
        const type = () => {
            if (i < text.length) {
                element.textContent += text.charAt(i);
                i++;
                setTimeout(type, speed);
            }
        };
        
        type();
    }
    
    // Stagger animation for lists
    staggerAnimation(elements, animationClass, delay = 100) {
        elements.forEach((element, index) => {
            setTimeout(() => {
                element.classList.add(animationClass);
            }, index * delay);
        });
    }
    
    // Show loading overlay
    showLoading() {
        let overlay = document.querySelector('.loading-overlay');
        if (!overlay) {
            overlay = document.createElement('div');
            overlay.className = 'loading-overlay';
            overlay.innerHTML = '<div class="spinner"></div>';
            document.body.appendChild(overlay);
        }
        overlay.classList.add('active');
    }
    
    // Hide loading overlay
    hideLoading() {
        const overlay = document.querySelector('.loading-overlay');
        if (overlay) {
            overlay.classList.remove('active');
        }
    }
    
    // Animate element entrance
    animateIn(element, animation = 'fadeIn', delay = 0) {
        element.style.opacity = '0';
        element.style.transform = this.getInitialTransform(animation);
        
        setTimeout(() => {
            element.style.transition = 'all 0.6s ease-out';
            element.style.opacity = '1';
            element.style.transform = 'translate(0, 0) scale(1)';
            element.classList.add(`animate-${animation}`);
        }, delay);
    }
    
    // Animate element exit
    animateOut(element, animation = 'fadeOut', callback) {
        element.style.transition = 'all 0.3s ease-in';
        element.style.opacity = '0';
        element.style.transform = this.getExitTransform(animation);
        
        setTimeout(() => {
            if (callback) callback();
        }, 300);
    }
    
    getInitialTransform(animation) {
        switch (animation) {
            case 'slideUp': return 'translateY(30px)';
            case 'slideDown': return 'translateY(-30px)';
            case 'slideLeft': return 'translateX(30px)';
            case 'slideRight': return 'translateX(-30px)';
            case 'scaleIn': return 'scale(0.8)';
            default: return 'translateY(20px)';
        }
    }
    
    getExitTransform(animation) {
        switch (animation) {
            case 'slideUp': return 'translateY(-30px)';
            case 'slideDown': return 'translateY(30px)';
            case 'slideLeft': return 'translateX(-30px)';
            case 'slideRight': return 'translateX(30px)';
            case 'scaleOut': return 'scale(0.8)';
            default: return 'translateY(-20px)';
        }
    }
    
    // Cleanup observers
    destroy() {
        this.observers.forEach(observer => observer.disconnect());
        this.observers.clear();
    }
}

// Initialize animation manager
let animationManager;

document.addEventListener('DOMContentLoaded', () => {
    animationManager = new AnimationManager();
});

// Export for use in other modules
window.AnimationManager = AnimationManager;

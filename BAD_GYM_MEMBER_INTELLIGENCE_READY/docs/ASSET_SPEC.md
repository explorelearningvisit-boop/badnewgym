# Asset Specification

members/
- original JPG: 800x1000, 4:5
- card WebP: 400x500
- thumbnail WebP: 200x250

products/
- transparent PNG: 600x600
- small PNG: 300x300

backgrounds/
- SVG, scalable, non-critical decorative backgrounds

icons/
- SVG, 24dp viewBox, currentColor

Rules:
- Keep original member photos separate from crops.
- Never bake critical text into images.
- Use WebP for Android photo thumbnails.
- Use PNG only where transparency is required.
- Use SVG/vector for icons and decorative geometry.
- Generate additional crops server-side or on upload.

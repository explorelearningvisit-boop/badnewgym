# BAD GYM — Member Intelligence Asset Manifest (MI-V5)

## 1. Asset Classification & Contract Table

| Asset Identifier | Type | Format | Source Path / Location | Dimensions / Aspect | Transparency | Intended Usage & Placement | Scaling Mode |
|---|---|---|---|---|---|---|---|
| `badgym_leaf_cluster_left` | Transparent Overlay | PNG (32-bit) | `res/drawable-nodpi/badgym_leaf_cluster_left.png` | 240×240 (1:1) | Yes (Alpha) | Natural Fresh theme ornament, anchored Bottom-Start | `ContentScale.FillBounds` / `Fit` |
| `badgym_leaf_cluster_right` | Transparent Overlay | PNG (32-bit) | `res/drawable-nodpi/badgym_leaf_cluster_right.png` | 240×240 (1:1) | Yes (Alpha) | Natural Fresh theme ornament, anchored Bottom-End | `ContentScale.FillBounds` / `Fit` |
| `badgym_leaf_pair_header` | Transparent Overlay | PNG (32-bit) | `res/drawable-nodpi/badgym_leaf_pair_header.png` | 120×120 (1:1) | Yes (Alpha) | Natural Fresh header decoration, right of brand title | `ContentScale.Fit` |
| `ic_bad_gym_bolt` | Vector Graphic | Android Vector XML | `res/drawable/ic_bad_gym_bolt.xml` | 24×24 dp (1:1) | Yes (Vector) | BAD GYM brand icon in header and navigation rail | `FitCenter` |
| `member_yash` | Portrait / Avatar | JPEG / Raster | `assets/media/member_yash.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Yash Patel - Silver/Gold) | `ContentScale.Crop` |
| `member_arjun` | Portrait / Avatar | JPEG / Raster | `assets/media/member_arjun.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Arjun Sharma - VIP) | `ContentScale.Crop` |
| `member_riya` | Portrait / Avatar | JPEG / Raster | `assets/media/member_riya.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Riya Sen - Gold) | `ContentScale.Crop` |
| `member_neha` | Portrait / Avatar | JPEG / Raster | `assets/media/member_neha.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Neha Kapoor - Corporate) | `ContentScale.Crop` |
| `member_kabir` | Portrait / Avatar | JPEG / Raster | `assets/media/member_kabir.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Kabir Mehta - Elite) | `ContentScale.Crop` |
| `member_aarav` | Portrait / Avatar | JPEG / Raster | `assets/media/member_aarav.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Aarav Nair - Normal) | `ContentScale.Crop` |
| `member_rohan` | Portrait / Avatar | JPEG / Raster | `assets/media/member_rohan.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Rohan Verma - Trial) | `ContentScale.Crop` |
| `member_simran` | Portrait / Avatar | JPEG / Raster | `assets/media/member_simran.jpg` | 800×920 (~1:1.15) | No | Member hero portrait (Simran Kaur - Premium) | `ContentScale.Crop` |
| `admin_avatar` | Portrait / Icon | JPEG / Raster | `assets/media/admin_avatar.jpg` | 400×400 (1:1) | No | Top-right admin profile indicator | `ContentScale.Crop` |
| `trainer_vikas` | Portrait / Avatar | JPEG / Raster | `assets/media/trainer_vikas.jpg` | 600×800 (3:4) | No | Trainer profile thumbnail in Trainer details menu | `ContentScale.Crop` |
| `gym_bg` | Background Texture | JPEG / Raster | `assets/media/gym_bg.jpg` | 1920×1080 (16:9) | No | Ambient gym interior texture for Glassmorphism/Dark themes | `ContentScale.Crop` |
| `workout_chest` | Content Photo | JPEG / Raster | `assets/media/workout_chest.jpg` | 800×600 (4:3) | No | Workout routine visual banner | `ContentScale.Crop` |
| `whey_product` | Product Photo | JPEG / Raster | `assets/media/whey_product.jpg` | 800×800 (1:1) | No | Supplement / store product banner | `ContentScale.Fit` |
| `attendance_progress_ring` | Programmatic | Compose Canvas / Arc | Runtime Component | `38.dp` × `38.dp` | Yes | Real-time circular percentage indicator | Dynamic drawArc |
| `workout_six_bar_progression` | Programmatic | Compose Canvas / Shapes | Runtime Component | `48.dp` × `24.dp` | Yes | 6-bar weekly workout level progression visual | Dynamic rounded rectangles |
| `theme_ornament_backgrounds` | Vector Material Pack | SVG / Compose Gradients | `assets/materials/theme_*.svg` | Scalable Vector | Yes | Theme-specific glowing vectors and ambient textures | Scalable |

## 2. Integrity Rules
- No raw file paths hardcoded inside presentation components; all media references pass through `MediaAssets.kt` or `R.drawable.*`.
- Overlays must always use `ContentScale.Fit` or `ContentScale.FillBounds` with explicit DP bounding boxes to prevent viewport expansion.
- Portraits must maintain consistent 1:1.15 rectangular aspect ratio with `14.dp` corner clipping.

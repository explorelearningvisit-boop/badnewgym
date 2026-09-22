BAD GYM ASSET PIPELINE

Directory plan:
assets/member_portraits/original
assets/member_portraits/card
assets/member_portraits/thumb
assets/theme_backgrounds
assets/theme_ornaments
assets/theme_logos
assets/icons
assets/illustrations

Portrait pipeline: original source -> 4:5 card crop -> 1:1 thumbnail -> optional transparent cutout. Keep headroom and shoulders. Face-aware crop preferred. Use Coil for image loading. Never bake UI labels into the portrait.

Theme backgrounds should be vectors or Compose drawing wherever practical so they scale cleanly. Raster assets are reserved for textures/cutouts.

Eight reference themes need their own background/ornament recipes, but they must share one component API.

No giant flattened screenshot is permitted as the UI.
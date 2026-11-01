# 3D Human Anatomy — Asset Research and License Gate

No 3D mesh is approved for production inclusion yet. A visually attractive model is not sufficient; commercial use, redistribution, modification, attribution, sub-mesh selection and mobile performance must all be cleared.

## Candidate sources to evaluate

1. BodyParts3D / Anatomography
   - Official project/source: https://lifesciencedb.jp/bp3d/
   - Review exact dataset and derivative asset license, attribution requirements and redistribution conditions.
   - Confirm mesh granularity and anatomical naming coverage.

2. Z-Anatomy
   - Project: https://www.z-anatomy.com/
   - Repository: https://github.com/Z-Anatomy/Anatomy-3D-Model
   - Inspect the exact license file and version of every downloaded asset.
   - Verify whether commercial app redistribution and modifications are allowed.

3. Sketchfab
   - https://sketchfab.com/
   - Per-model licensing varies. Use only a model whose explicit license permits commercial use, modifications and embedding/redistribution in the intended app.
   - Do not assume "downloadable" means commercially reusable.

4. Commercial vendors
   - Evaluate only after obtaining written license terms for app embedding, commercial redistribution, derivative meshes, platform/runtime and update rights.

## Technical acceptance criteria

- GLB/glTF or another mobile-supported format.
- Stable IDs for each selectable structure.
- Separate meshes/material groups for relevant muscles and subdivisions.
- Front, back and side views from one model.
- Selectable structures can be highlighted without corrupting neighboring meshes.
- LOD, compressed textures and polygon budget suitable for low/mid-range Android.
- Slow idle rotation, touch pause, manual orbit, zoom and reduced-motion/static fallback.
- Asset loading does not block core member/workout screens.
- No diagnostic or clinical claims from the visual model.

## Runtime evaluation

The production app is Kotlin + Jetpack Compose. Evaluate Android-native rendering (e.g. Filament or an appropriate Android 3D runtime) first. Three.js is suitable for a web prototype or embedded web surface, but should not be introduced into the native app without a measured reason.

## Gate before download/commit

- [ ] Exact asset URL and version recorded.
- [ ] License file archived with asset.
- [ ] Commercial redistribution confirmed.
- [ ] Attribution requirements recorded.
- [ ] Mesh structure and anatomical accuracy reviewed.
- [ ] File size and Android performance tested.
- [ ] Stable anatomy IDs mapped.
- [ ] Legal/technical approval recorded.

Until every gate is satisfied, use a clearly labelled placeholder/prototype and do not commit an unlicensed mesh.

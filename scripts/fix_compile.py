import os

files = [
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/NutritionMenu.kt',
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/ServicesMenu.kt',
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/SupplementsMenu.kt',
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/WorkoutMenu.kt',
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/HistoryMenu.kt',
    'app/src/main/java/com/example/badnewgym/feature/memberintelligence/presentation/components/InsightMenu.kt'
]

for file in files:
    with open(file, 'r') as f:
        content = f.read()

    content = content.replace('import com.example.badnewgym.feature.memberintelligence.design.dimensions.Spacing', 'import androidx.compose.ui.unit.dp\nimport androidx.compose.material3.Text')
    content = content.replace('verticalArrangement = Arrangement.spacedBy(Spacing.md)', 'modifier = modifier.fillMaxSize().padding(BADGymTheme.dimensions.outerPadding)')
    content = content.replace('horizontalArrangement = Arrangement.spacedBy(Spacing.sm)', 'horizontalArrangement = Arrangement.spacedBy(8.dp)')
    content = content.replace('title =', 'label =')
    
    # Replace EventHeader
    content = content.replace('EventHeader(', 'Text(style = BADGymTheme.typography.kpiValue, color = BADGymTheme.colors.textPrimary, text =')
    content = content.replace(',\n            subtitle =', ' // ')
    content = content.replace('\" // \"', '\"')
    
    with open(file, 'w') as f:
        f.write(content)

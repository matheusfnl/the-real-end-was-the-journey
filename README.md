# The Real End Was the Journey

The Real End Was the Journey is a Minecraft mod that adds new Eyes of Ender crafted with items
found across different biomes, encouraging players to explore the world before
reaching the endgame.

## Setup

Install a **JDK 25** and set `JAVA_HOME` to its installation directory (not its
`bin` directory). Each developer uses their own local path; do not commit
`org.gradle.java.home` or other machine-specific JDK paths to this repository.

### Windows (PowerShell)

Replace the example path with your installed JDK 25 directory:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-25"
& "$env:JAVA_HOME\bin\java.exe" -version
.\gradlew.bat runClient
```

To persist the setting for future terminals, run
`setx JAVA_HOME "C:\path\to\jdk-25"`, then restart your terminal and IDE.
`setx` does not update the current terminal session.

### Linux / macOS

```sh
export JAVA_HOME="/path/to/jdk-25"
"$JAVA_HOME/bin/java" -version
./gradlew runClient
```

On macOS, you can select an installed JDK 25 with
`export JAVA_HOME="$(/usr/libexec/java_home -v 25)"`.
Add the export to your shell configuration to persist it.

### Other tasks

Use `.\gradlew.bat build` to build the mod and `.\gradlew.bat runDatagen` to
generate resources on Windows. On Linux/macOS, use `./gradlew build` and
`./gradlew runDatagen`.

Configure your IDE's Gradle JVM to use JDK 25 as well. Local VS Code settings
in `.vscode/` are ignored by Git. If Gradle still selects a different JDK,
check your user-level `~/.gradle/gradle.properties` for an
`org.gradle.java.home` override.

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.

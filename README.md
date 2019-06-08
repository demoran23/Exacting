# Exacting

Exacting is a mod for Slay The Spire, inspired by the mechanics of NEOVERSE.

Blocking an opponent's attack exactly will debuff your opponent.

Taking an opponent to exactly 0 hp will yield a reward.

# Building

run `./gradlew copyJarToStsMods` to build.  This requires an environment variable `STS_HOME` to be set, which is your install directory for Slay The Spire.

In IntelliJ IDEA, you can create a run configuration and pin the variable to it.

# Pulling

The jars in the `lib` folder are stored in LFS, so you'll need to have that installed.  If you don't, the files will be there after pull, but they'll be like 1kb.

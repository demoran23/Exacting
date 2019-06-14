# Exacting

Exacting is a mod for Slay The Spire, inspired by the mechanics of NEOVERSE.

Taking an opponent to exactly 0 hp will yield a reward.

Blocking an opponent's attack exactly will debuff the opponent.

Making an attack equal to an opponent's defense will buff the opponent.

# Rules
Exacting adds a few rules centered around making exact blocks and exact attacks.
Please note that this document may lose comprehensiveness over time.  
See `ExactAttack.kt` and `ExactBlock.kt` for more details. 

## Exact Block
If an opponent attacks you and your block is a match for their attack damage, they have a chance to suffer one of the following:

* Stun
* Weakness
* Vulnerability
* Vulnerability and Weakness

## Exact Attack
### Attack Kills Monster Exactly
If your attack damage brings an opponent to exactly zero hp, you have a chance to gain the following:

* +2 Max Hp
* Relic
* Card
* Potion
* Heal
* Energy
* Gold

The chance for an individual reward is scaled according to the type of enemy (eg normal, elite, boss).

### Attack Equals Defense
If your attack damage matches an opponent's defense exactly, they will receive one of the following:

* Intangibility
* Healing
* Block
* Strength
* Dexterity

# Building

run `./gradlew copyJarToStsMods` to build.  This requires an environment variable `STS_HOME` to be set, which is your install directory for Slay The Spire.

In IntelliJ IDEA, you can create a run configuration and pin the variable to it.

# Pulling

The jars in the `lib` folder are stored in LFS, so you'll need to have that installed.  If you don't, the files will be there after pull, but they'll be like 1kb.

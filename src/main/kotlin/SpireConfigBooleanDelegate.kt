package exacting

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import kotlin.reflect.KProperty

class SpireConfigBooleanDelegate {
    companion object {
        val spireConfig = SpireConfig("exacting", "exacting.config")
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): kotlin.Boolean {
        val configKey = property.getAnnotation<ConfigKey>()
        return spireConfig.getBool(configKey.Key)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: kotlin.Boolean) {
        val configKey = property.getAnnotation<ConfigKey>()
        spireConfig.setBool(configKey.Key, value)
        spireConfig.save()
    }
}

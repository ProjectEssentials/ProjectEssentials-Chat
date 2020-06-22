package com.mairwunnx.projectessentials.chat.api.validator

import com.mairwunnx.projectessentials.chat.impl.validators.DefaultChatValidator

/**
 * Class for interacting with active chat validator.
 * @since 2.0.0.
 */
object ChatValidatorAPI {
    /**
     * Active chat validator, you can change that at any time.
     *
     * By default that have value [DefaultChatValidator].
     *
     * @since 2.0.0.
     */
    var validator: IChatValidator = DefaultChatValidator
}

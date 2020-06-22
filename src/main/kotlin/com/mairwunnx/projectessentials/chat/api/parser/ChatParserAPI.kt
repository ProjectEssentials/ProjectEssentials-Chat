package com.mairwunnx.projectessentials.chat.api.parser

import com.mairwunnx.projectessentials.chat.impl.parsers.DefaultMessageParser

/**
 * Class for interacting with active chat parser.
 * @since 2.0.0.
 */
object ChatParserAPI {
    /**
     * Active chat parser, you can change that at any time.
     *
     * By default that have value [DefaultMessageParser].
     *
     * @since 2.0.0.
     */
    var parser: IMessageParser = DefaultMessageParser()
}

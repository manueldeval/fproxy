package org.deman.fproxy.config.rules;

import org.deman.fproxy.config.Rule;

public class NoRule extends Rule {
    public NoRule() {
        super("NoMatch", p -> true);
    }
}

package org.deman.fproxy;

import io.vavr.collection.List;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.deman.fproxy.config.Rule;
import org.deman.fproxy.config.rules.DirectRule;
import org.deman.fproxy.config.rules.DiscardRule;
import org.deman.fproxy.config.rules.NoRule;
import org.deman.fproxy.config.rules.UpstreamRule;
import org.deman.fproxy.http.Prolog;
import org.deman.fproxy.proxyhandlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class RuleRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleRouter.class);

    private Vertx vertx;
    private List<Rule> rules;

    public RuleRouter(Vertx vertx, List<Rule> rules) {
        this.vertx = vertx;
        this.rules = rules;
    }


    private Rule findRule(List<Rule> rules, Prolog prolog) {
        return rules
            .filter(rule -> rule.accept(prolog))
            .headOption()
            .getOrElse(NoRule::new);
    }

    public void route(NetSocket browserSocket, Prolog prolog) {
        Rule rule = findRule(rules, prolog);
        LOGGER.debug("Rule used: {}", rule.getName());
        ProxyHandler handler = Match(rule).of(
            Case($(instanceOf(DirectRule.class)), () -> new DirectProxyHandler(vertx)),
            Case($(instanceOf(UpstreamRule.class)), () -> new UpstreamProxyHandler(vertx, (UpstreamRule) rule)),
            Case($(instanceOf(DiscardRule.class)), DiscardProxyHandler::new),
            Case($(), NoRuleProxyHandler::new));
        handler.handle(browserSocket, prolog);
    }

}

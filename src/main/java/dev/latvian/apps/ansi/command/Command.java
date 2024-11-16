package dev.latvian.apps.ansi.command;

import java.util.List;

public record Command(String name, List<String> args, CommandCallback callback) {
}
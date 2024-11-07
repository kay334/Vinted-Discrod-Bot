# Vinted Discord Bot
A bot that scrapes items from Vinted and posts updates to a Discord channel using webhooks.

## Features
- Scrapes items from Vinted
- Sends updates to Discord with embeds
- Allows users to add/remove subscriptions to Vinted URLs

## Setup
1. Clone the repository.
2. Update the `config.json` with your Discord bot token and webhook URL.
3. Run the bot using a Java IDE or command line.

## Requirements
- Java 8 or higher
- Dependencies: Jsoup, JDA

## Usage
- Use `$sub <vinted_url>` to add a subscription.
- Use `$remove_sub` to remove the current subscription.
- Use `$list_subs` to list active subscriptions.
- Use `$help` to display command help.

## License
MIT License

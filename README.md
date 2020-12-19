# This is the main program
Out of the various prototypes this version is the best working program. It scrapes an RSS feed, saves it to an archive,
compares the two, and what ever is new is then sent via SMS to recipient(s). Its main limitation is that only RSS feeds
(XML) can be utilized. The focus of this program is to scan for relevant news fast. Either one service provide needs to
be found to acquire all needed news sources, or API's found for each needed news source. I also need to figure out if the
JSON format or RSS feeds will be most readily available, and the fastest option.

# Need to do
- SEC.gov provides various RSS feeds that would need to be scraped and filtered to be useful.
- otcmarkets - not sure yet
- scrape for other significant news that are not PR's, otcmarket, or SEC related, prioritize, archive, and send via email?
- create logging errors to be sent via email
- Need to decide between RSS feeds or JSON. Which provider of news will be the fastest and provide the most news, which format will it come in?
  JSON or RSSS?
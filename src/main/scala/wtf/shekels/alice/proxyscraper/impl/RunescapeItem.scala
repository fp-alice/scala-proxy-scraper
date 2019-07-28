package wtf.shekels.alice.proxyscraper.impl

case class RunescapeItem(item: String,
                         icon: String,
                         icon_large: String,
                         itemType: String,
                         typeIcon: String,
                         name: String,
                         members: Boolean,
                         trend: String,
                         price: String,
                         change: String,
                         current: List[String],
                         today: List[String],
                         day30: List[String],
                         day90: List[String],
                         day180: List[String]
                        )

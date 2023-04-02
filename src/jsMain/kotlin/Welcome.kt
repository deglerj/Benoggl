import client.RSocketClient
import csstype.px
import csstype.rgb
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jd.benoggl.common.models.Game
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import web.html.InputType

external interface WelcomeProps : Props {
    var name: String
}


val Welcome = FC<WelcomeProps> { props ->

    val (test, setTest) = useState("")

    val scope = useMemo(dependencies = emptyArray()) { MainScope() }

    useEffect(dependencies = emptyArray()) {
        console.log("EFFECT")
        scope.launch {
            RSocketClient.requestEventStream()
                .onEach {
                    console.log(it)
                    setTest(it.explain(Game()))
                }
                .launchIn(scope)
        }
    }

    var name by useState(props.name)

    div {
        css {
            padding = 5.px
            backgroundColor = rgb(8, 97, 22)
            color = rgb(56, 246, 137)
        }
        +"Hello!!, $name"
    }
    input {
        css {
            marginTop = 5.px
            marginBottom = 5.px
            fontSize = 14.px
        }
        type = InputType.text
        value = name
        onChange = { event ->
            name = event.target.value
        }
    }
    div {
        +"LAST TEST: $test"
    }
}
from com.insightfullogic.stripper import MethodNameRegex, ReturnTypeName, ArgumentTypeName

rules = [
    MethodNameRegex("toString"),
    ReturnTypeName("I"),
    ArgumentTypeName("I", 0)
]
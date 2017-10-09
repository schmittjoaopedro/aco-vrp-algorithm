library(plotly)

data <- matrix(ncol = 5, byrow = T, data = c(
))

data <- data.frame(time = data[,1], worst = data[,4], mean = data[,2], best = data[,3], bestSF = data[,5])

# Worst curve
plot_ly(data = data, x = ~time, y = ~worst, mode = 'lines', type = "scatter") %>%
    layout(title = "Worst", yaxis = list(title = "Meters"), xaxis = list(title = "Iteration"))
# Mean curve
plot_ly(data = data, x = ~time, y = ~mean, mode = 'lines', type = "scatter") %>%
    layout(title = "Mean", yaxis = list(title = "Meters"), xaxis = list(title = "Iteration"))
# Best curve
plot_ly(data = data, x = ~time, y = ~best, mode = 'lines', type = "scatter") %>%
    layout(title = "Best", yaxis = list(title = "Meters"), xaxis = list(title = "Iteration"))
# Best so far curve
plot_ly(data = data, x = ~time, y = ~bestSF, mode = 'lines', type = "scatter") %>%
    layout(title = "Best so far", yaxis = list(title = "Meters"), xaxis = list(title = "Iteration"))
# All plots
plot_ly(data = data, x = ~time, y = ~worst, mode = 'lines', type = "scatter", name = 'Worst') %>%
    add_trace(y = ~mean, mode = 'lines', name = 'Mean') %>%
    add_trace(y = ~best, mode = 'lines', name = 'Best') %>%
    add_trace(y = ~bestSF, mode = 'lines', name = 'Best so far')

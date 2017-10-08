library(plotly)

data <- matrix(ncol = 5, byrow = T, data = c(
))

data <- data.frame(time = data[,1], worst = data[,4], mean = data[,2], best = data[,3], bestSF = data[,5])

# Worst curve
plot_ly(data = data, x = ~time, y = ~worst, mode = 'lines', type = "scatter")
# Mean curve
plot_ly(data = data, x = ~time, y = ~mean, mode = 'lines', type = "scatter")
# Best curve
plot_ly(data = data, x = ~time, y = ~best, mode = 'lines', type = "scatter")
# Best so far curve
plot_ly(data = data, x = ~time, y = ~bestSF, mode = 'lines', type = "scatter")
# All plots
plot_ly(data = data, x = ~time, y = ~worst, mode = 'lines', type = "scatter") %>% 
    add_trace(y = ~mean, mode = 'lines') %>% 
    add_trace(y = ~best, mode = 'lines') %>% 
    add_trace(y = ~bestSF, mode = 'lines')

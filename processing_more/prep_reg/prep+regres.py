# -*- coding: utf-8 -*-
"""21Z205_prep+regres.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/18g0Gz0kVNXMecYIaxKigoQvJ3WbQUuWt

# Intaling Required libaries
"""

!pip install numpy
!pip install pandas
!pip install sklearn
!pip install seaborn
!pip install matplotlib

"""# Import statements

"""

import numpy as np
import pandas as pd
from sklearn.impute import SimpleImputer
from sklearn.preprocessing import Normalizer, OneHotEncoder
from sklearn.preprocessing import LabelEncoder
from sklearn.compose import ColumnTransformer
from datetime import datetime
import seaborn as sns
import matplotlib.pyplot as plt
from scipy.stats import zscore

"""# Imporing Dataset"""

# Read the dataset
dataset = pd.read_csv(r"avocado.csv")

# Store a copy of the dataset for later use
dat = dataset.copy()

"""# Filling Missing Values

"""

for yr in dat['year'].unique():
    for rg in dat['region'].unique():
        # Filter the DataFrame based on 'year' and 'region'
        condition = (dat['year'] == yr) & (dat['region'] == rg)
        filtered_data = dat[condition].copy()  # Make a copy to avoid SettingWithCopyWarning

        # Exclude non-numeric columns from the imputation process
        numeric_columns = ['AveragePrice', 'Total Volume', '4046', '4225', '4770', 'Total Bags', 'Small Bags', 'Large Bags', 'XLarge Bags']
        filtered_data[numeric_columns] = filtered_data[numeric_columns].apply(pd.to_numeric, errors='coerce')

        # Initialize and fit the imputer
        imputer = SimpleImputer(missing_values=np.nan, strategy='mean')
        imputer.fit(filtered_data[numeric_columns])

        # Transform and assign values back to the DataFrame
        dat.loc[condition, numeric_columns] = imputer.transform(filtered_data[numeric_columns])

"""# Convert date to number of days from"""

def crt_dat(dt):
    tem= datetime.strptime(dt, "%Y-%m-%d")
    epoch = datetime(2015, 1, 1)
    days_from_epoch = (tem - epoch).days
    return days_from_epoch

print(dat['region'].isna().any())
dat['Date']= dat['Date'].apply(crt_dat)

"""# Defining function to remove outliers"""

def remove_outliers_iqr(data):
    Q1 = np.percentile(data, 25)
    Q3 = np.percentile(data, 75)
    IQR = Q3 - Q1
    lower_bound = Q1 -( 1.5 * IQR ) # Adjusted multiplier to 1.5 for typical IQR outlier rule
    upper_bound = Q3 + (1.5 * IQR  )# Adjusted multiplier to 1.5 for typical IQR outlier rule
    def rm(dt):
        if (dt < lower_bound) or (dt > upper_bound):
            return np.nan
        return dt
    return data.apply(rm)

# Function to remove outliers using z-score method
def remove_outliers_zscore(data):
    numeric_data = data.select_dtypes(include=[np.number])  # Select only numeric columns
    z_scores = zscore(numeric_data)
    abs_z_scores = np.abs(z_scores)
    filtered_entries = (abs_z_scores < 3).all(axis=1)  # Adjust the threshold as needed
    return data[filtered_entries]

columns_to_normalize = ['AveragePrice', '4046', '4225', '4770', 'Total Bags', 'Small Bags', 'Large Bags', 'XLarge Bags','Total Volume']
columns_to_normalize_tw = [ '4046',  '4770',  'Large Bags']

"""# Data before Normalisation

"""

plt.figure(figsize=(8, 6))
# plt.boxplot(dat, labels=columns_to_normalize, patch_artist=True, showmeans=True)
sns.boxplot(data=dat[columns_to_normalize])
plt.title('Box Plot Example')
plt.ylabel('Values')
plt.show()

"""# Removing Outliers"""

# dat = remove_outliers_zscore(dat)

for i in columns_to_normalize:
    dat[i] = remove_outliers_iqr(dat[i])
    # dat = dat.dropna(subset=[i])
dat = dat.dropna()
# for i in columns_to_normalize_tw:
#     dat[i] = remove_outliers_iqr(dat[i])
#     dat = dat.dropna(subset=[i])

# for j in range(0,15):
#     for i in columns_to_normalize_tw:
#         dat[i] = remove_outliers_iqr(dat[i])
#         dat = dat.dropna(subset=[i])
# dat.to_csv('avooutl.csv')
dat.reset_index(drop=True, inplace=True)

"""# Normalise Data"""

columns_to_normalize.append("Date")
tem = dat[columns_to_normalize]
# print(tem)
norm = Normalizer()

tem = norm.fit_transform(tem)
# print(tem)

dat.reset_index(drop=True, inplace=True)

dat[columns_to_normalize] = pd.DataFrame(tem, columns=columns_to_normalize)

print(pd.DataFrame(tem, columns=columns_to_normalize))
columns_to_normalize_r = ['remainder__AveragePrice', 'remainder__4046', 'remainder__4225', 'remainder__4770', 'remainder__Total Bags', 'remainder__Small Bags', 'remainder__Large Bags', 'remainder__XLarge Bags','remainder__Total Volume']

"""# One Hotencoding"""

label_encoder = LabelEncoder()

# Fit label encoder and transform the specified column
dat['type'] = label_encoder.fit_transform(dat['type'])




ct = ColumnTransformer(transformers=[('encoder', OneHotEncoder(), ['region','year'])], remainder='passthrough')

# Fit and transform the data
transformed_data = ct.fit_transform(dat)
transformed_data_dense = transformed_data.toarray()

# Convert the dense array to a DataFrame
dat = pd.DataFrame(transformed_data_dense, columns=ct.get_feature_names_out())
# print(dat)

"""# Data after Preprocessing"""

plt.figure(figsize=(8, 6))
    # plt.boxplot(dat, labels=columns_to_normalize, patch_artist=True, showmeans=True)
sns.boxplot(data=dat[columns_to_normalize_r])
plt.title(columns_to_normalize_r)
plt.ylabel('Values')
plt.show()

"""Total Volume vs Total Bags"""

plt.figure(figsize=(10, 6))
plt.scatter(dat['remainder__Total Volume'], dat['remainder__Total Bags'], alpha=0.5)
plt.title(' Volumes over Bags')
plt.xlabel('Total Volume')
plt.ylabel('Total Bags')
plt.show()

"""# Year vs AvgPrice"""

year_columns = ['encoder__year_2015', 'encoder__year_2016', 'encoder__year_2017', 'encoder__year_2018']
average_price_column = 'remainder__AveragePrice'

# Extract relevant columns
df_year_avgprice = dat[year_columns + [average_price_column]]

# Melt the DataFrame to have years as one column and average price as another column
df_year_avgprice_melted = df_year_avgprice.melt(value_vars=year_columns, var_name='Year', value_name='AveragePrice')

# Group by year and calculate the average price
average_price_by_year = df_year_avgprice_melted.groupby('Year')['AveragePrice'].mean()

# Plot the average price over the years
plt.figure(figsize=(10, 6))
average_price_by_year.plot(kind='line', marker='o', color='b')
plt.title('Average Price Over the Years')
plt.xlabel('Year')
plt.ylabel('Average Price')
plt.xticks(rotation=45)
plt.grid(True)
plt.tight_layout()
plt.show()

plt.hist(dat['remainder__AveragePrice'], bins=30, color='skyblue', edgecolor='black')
plt.title('Histogram of AveragePrice' )
plt.xlabel('AveragePrice')
plt.ylabel('Frequency')
plt.show()


plt.hist(dat["remainder__Total Volume"], bins=30, color='skyblue', edgecolor='black')
plt.title('Histogram of Total Volume' )
plt.xlabel("Total Volume")
plt.ylabel('Frequency')
plt.show()


plt.hist(dat["remainder__Total Bags"], bins=30, color='skyblue', edgecolor='black')
plt.title('Histogram of Total Bags' )
plt.xlabel("Total Bags")
plt.ylabel('Frequency')
plt.show()

"""#Print final data"""

dat.to_csv(r'avocado_processed.csv')
print(dat.columns)

"""#import liberies for regression"""

from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import statsmodels.api as sm

"""# Spliting the data and performing regression"""

# Splitting the data into features (X) and target variable (Y)
X = dat.drop(columns=['remainder__AveragePrice'])  # Features
Y = dat['remainder__AveragePrice']  # Target variable

# Splitting the data into training and testing sets
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=42)

# Creating a linear regression model
model = LinearRegression()

# Training the model
model.fit(X_train, Y_train)

"""# Predict Using Test data and calculate Mean Squared Error"""

# Making predictions
Y_pred = model.predict(X_test)

# Calculating mean squared error
mse = mean_squared_error(Y_test, Y_pred)
print("Mean Squared Error:", mse)

"""#Scatterplots independent variable against the dependent variable"""

for column in X.columns:
    plt.scatter(X[column], Y)
    plt.title(f"Scatterplot of {column} vs Price")
    plt.xlabel(column)
    plt.ylabel("Price")
    plt.show()

"""#Print Correlation Matrix"""

print(dat.columns)
print()
corr_coloums = ['remainder__Date', 'remainder__AveragePrice',
       'remainder__Total Volume', 'remainder__4046', 'remainder__4225',
       'remainder__4770', 'remainder__Total Bags', 'remainder__Small Bags',
       'remainder__Large Bags', 'remainder__XLarge Bags', 'remainder__type'] #coloums except regions
# Calculating correlation matrix
correlation_matrix = dat[corr_coloums].corr()
rowcor = dat['remainder__AveragePrice']
correlations = {}
#calculate correlation
for column in corr_coloums:
  if column=='remainder__AveragePrice':
    continue
  correlations[column] = rowcor.corr(dat[column])


# Print the correlations
print(correlations)
#seperate positive and negative correlated items
final_pos_features = [col for col, corr in correlations.items() if corr > 0]
final_neg_features = [col for col, corr in correlations.items() if corr < 0]


# Print correlation matrix
plt.figure(figsize=(10, 8))
sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm', fmt=".2f")
plt.title('Correlation Matrix')
plt.show()

"""#Using corelation finding threshold to find rreveland features"""

positive_corr_sum = 0
positive_corr_count = 0
negative_corr_sum = 0
negative_corr_count = 0

# Loop over correlation coefficients
for corr in correlations.values():
    if corr > 0:
        positive_corr_sum += corr
        positive_corr_count += 1
    elif corr < 0:
        negative_corr_sum += corr
        negative_corr_count += 1

# Calculate average of positive and negative correlations
average_positive_corr = positive_corr_sum / positive_corr_count if positive_corr_count > 0 else 0
average_negative_corr = negative_corr_sum / negative_corr_count if negative_corr_count > 0 else 0

print("Average of positive correlation coefficients:", average_positive_corr)
print("Average of negative correlation coefficients:", average_negative_corr)

"""# Extract positive and negative corelated features array"""

final_positive_features = []
final_negative_features = []

# Loop over correlation coefficients again to identify final features
for key, corr in correlations.items():
    if corr > average_positive_corr:
        final_positive_features.append(key)
    elif corr < average_negative_corr:
        final_negative_features.append(key)

print("Final positive features:", final_positive_features)
print("Final negative features:", final_negative_features)

final_features = final_positive_features + final_negative_features

"""# strength of the relationship between independent and dependent variables

Using all features
"""

X = dat[corr_coloums].drop(columns =['remainder__AveragePrice']) # Features

Y = dat['remainder__AveragePrice']  # Target variable

# Add constant term for intercept
X = sm.add_constant(X)

# Fit the linear regression model
model = sm.OLS(Y, X).fit()

# Print the summary statistics
print(model.summary())

"""Using final features extracted"""

X = dat[final_features] # Features
Y = dat['remainder__AveragePrice']  # Target variable

# Add constant term for intercept
X = sm.add_constant(X)

# Fit the linear regression model
model = sm.OLS(Y, X).fit()

# Print the summary statistics
print(model.summary())

"""# Using positive,negative feeatures"""

X = dat[final_pos_features]# Features
Y = dat['remainder__AveragePrice']  # Target variable

# Add constant term for intercept
X = sm.add_constant(X)

# Fit the linear regression model
model = sm.OLS(Y, X).fit()

# Print the summary statistics
print(model.summary())

X = dat[final_neg_features] # Features
Y = dat['remainder__AveragePrice']  # Target variable

# Add constant term for intercept
X = sm.add_constant(X)

# Fit the linear regression model
model = sm.OLS(Y, X).fit()

# Print the summary statistics
print(model.summary())

"""# strength of the relationship between independent and dependent variables"""

for i in corr_coloums:
  X = dat[i] # Features
  Y = dat['remainder__AveragePrice']  # Target variable

  # Add constant term for intercept
  X = sm.add_constant(X)

  # Fit the linear regression model
  model = sm.OLS(Y, X).fit()

  # Print the summary statistics
  print(model.summary())

"""#Final Features

Depending on the observaation of various independend variable and corelatioon with the dependend variable.We chose the most relevant features and find the results.
"""

temp_arr = ["remainder__4046","remainder__4225","remainder__4770","remainder__Small Bags",'remainder__type',"remainder__Date",'remainder__Large Bags']
# tem2 = ["remainder__Date",'remainder__Total Volume',"remainder__4046","remainder__4225","remainder__4770",'remainder__Total Bags','remainder__Small Bags','remainder__Large Bags']
# tem3 = ['remainder__Total Volume','remainder__type',"remainder__Date"]
X = dat[temp_arr] # Features
Y = dat['remainder__AveragePrice']  # Target variable

# Add constant term for intercept
X = sm.add_constant(X)

# Fit the linear regression model
model = sm.OLS(Y, X).fit()

# Print the summary statistics
print(model.summary())

"""# Inference

From the above expriment we find that certain features like

-> remainder_XLarge bags are weakly related with Average price as they tend to buy in lage number of large and small bags

-> the Total bags are not included because it is aproximate equal to sum of large and small bags

-> Similarly Total Voloume is samll as sum of individual types of avacado So it was excluded

# Conclusion

Thus by removing weakly corelated and irrevelant features we could increase our accuracy to 53.7% by also using less number of variables into account and also removed multicolinearity from our database.
"""